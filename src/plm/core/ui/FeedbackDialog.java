package plm.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.lesson.Lecture;
import plm.core.model.tracking.GitHubAuth;
import plm.core.model.tracking.GitUtils;
import plm.core.ui.action.GitHubLogin;
import plm.universe.World;

public class FeedbackDialog extends JFrame {

  private static final long serialVersionUID = 0;
  private static FeedbackDialog instance     = null;
  private static String defaultTitle         = "";
  private static final Pattern TITLE_PREFIX  = Pattern.compile("^\\[\\w+\\] ");
  private static final Pattern ID_LINE       = Pattern.compile("(?m)^Public user ID: PLM\\w+\\R");

  public String errorMsg;
  final JEditorPane feedback = new JEditorPane();
  final JTextField title     = new JTextField();
  final ButtonGroup categories = new ButtonGroup();
  private final JPanel categoryPanel = new JPanel(new GridLayout(0, 1));
  private Category category;
  private String techInfo  = "";
  private String lastBody  = "";
  final JButton sendBtn    = new JButton();
  final JButton connectBtn = new JButton(new GitHubLogin(Game.getInstance(), Game.i18n.tr("Connect to GitHub"), null, this));

  public static FeedbackDialog getInstance()
  {
    if (FeedbackDialog.instance == null) {
      FeedbackDialog.instance = new FeedbackDialog();
    }
    StringBuffer worldInfo = new StringBuffer();
    Exercise current       = (Exercise)Game.getInstance().getCurrentLesson().getCurrentExercise();
    for (World w : current.getWorlds(WorldKind.ANSWER)) {
      String s = w.getDebugInfo();
      if (!s.isEmpty())
        worldInfo.append("World: " + s + "\n");
    }
    // The randomized worlds cannot be rebuilt by the maintainers to reproduce the problem
    current.serializeRandomizedWorlds().forEach((rank, map) -> worldInfo.append("Randomized initial world " + rank + ":\n" + map));

    defaultTitle       = Game.i18n.tr("Please describe the problem in a few words");
    Lecture exo        = Game.getInstance().getCurrentLesson().getCurrentExercise();
    String proposedExo = "";
    if (exo instanceof Exercise exercise) {
      ProgrammingLanguage lang = Game.getInstance().getProgrammingLanguage();
      proposedExo = "\n" + Game.i18n.tr("--------------------[ my code for this exercise ]--------------------\n") + exercise.getEditorContent(lang);
    }

    FeedbackDialog.instance.techInfo =
        Game.i18n.tr("(The following helps us fixing your problem, please don't erase)\n\n") + /* The rest is not translated */
        "--------------------[ Technical Information ]--------------------\n"
        + "Lesson: " + Game.getInstance().getCurrentLesson().getId() + "\n"
        + "Exercise: " + Game.getInstance().getCurrentLesson().getCurrentExercise().getId() + "\n" + worldInfo.toString() +
        "Programming Language: " + Game.getInstance().getProgrammingLanguage().getLang() + "\n"
        + "Locale: " + Game.getInstance().getLocale().getDisplayName() + "\n"
        + "Java version: " + System.getProperty("java.version") + " (VM: " + System.getProperty("java.vm.name") +
        "; version: " + System.getProperty("java.vm.version") + ")"
        + "\n"
        + "OS: " + System.getProperty("os.name") + " (version: " + System.getProperty("os.version") + "; arch: " + System.getProperty("os.arch") + ")"
        + "\n"
        + "PLM version: " + Game.getProperty("plm.major.version", "internal", false) + " (" + Game.getProperty("plm.minor.version", "internal", false) +
        "), commit " + Game.getGitCommit() + "\n"
        + "Public user ID: PLM" + GitUtils.sha1(Game.getInstance().getUsers().getCurrentUser().getUserUUIDasString()) + "\n" + proposedExo;

    FeedbackDialog.instance.title.setText(defaultTitle);
    FeedbackDialog.instance.category = null;
    FeedbackDialog.instance.categories.clearSelection();
    FeedbackDialog.instance.categoryPanel.setVisible(true);
    FeedbackDialog.instance.feedback.setText("");
    FeedbackDialog.instance.lastBody = "";
    FeedbackDialog.instance.refreshForm();
    FeedbackDialog.instance.pack();
    return FeedbackDialog.instance;
  }

  private FeedbackDialog()
  {
    super(Game.i18n.tr("Report your feedback"));
    initComponent();
  }

  public void initComponent()
  {

    setLayout(new BorderLayout());
    JPanel headerToolbar = new JPanel(new BorderLayout(5, 0));
    headerToolbar.add(new Label(Game.i18n.tr("Issue title:")), BorderLayout.WEST);
    headerToolbar.add(title, BorderLayout.CENTER);

    JEditorPane hints = PlmHtmlEditorKit.pane();
    hints.setText(
        Game.i18n.tr("<p>Questions are better asked on our <a href='https://discord.gg/agxNPGj'>Discord server</a>. "
                     + "To report something here, choose a category below, and complete the proposed text, in English or French if possible.</p>\n"
                     + "<p>Your report will be <b>public</b>: never disclose passwords or other sensitive information. "
                     + "It is anonymous unless you connect to GitHub; an anonymous report contains a public user ID (a line that you can delete).</p>"));
    hints.addHyperlinkListener(new HyperlinkListener() {
      public void hyperlinkUpdate(HyperlinkEvent event)
      {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
          String uri = event.getDescription();

          try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
              Desktop.getDesktop().browse(new URI(uri));
            } else if (new File("/usr/bin/xdg-open").exists()) {
              Runtime.getRuntime().exec("/usr/bin/xdg-open " + uri);
            } else if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
              Runtime.getRuntime().exec("open " + uri);
            } else {
              System.err.println("Cannot find any browser");
            }
          } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
          }
        }
      }
    });

    for (Category c : Category.values()) {
      JRadioButton radio = new JRadioButton(c.label());
      radio.addActionListener(e -> selectCategory(c));
      categories.add(radio);
      categoryPanel.add(radio);
    }
    JPanel north = new JPanel();
    north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
    north.add(hints);
    north.add(categoryPanel);
    north.add(headerToolbar);
    add(north, BorderLayout.NORTH);

    feedback.setBackground(Color.white);
    feedback.setOpaque(true);
    feedback.setEditable(true);
    JScrollPane jsp = new JScrollPane(feedback);
    jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    add(jsp, BorderLayout.CENTER);

    feedback.setContentType("text/plain");

    final JButton cancelBtn = new JButton(Game.i18n.tr("Cancel"));
    cancelBtn.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent e)
      {
        int dialogResult = JOptionPane.showConfirmDialog(cancelBtn, Game.i18n.tr("Do you really want to cancel your feedback and lose any edit?"),
                                                         Game.i18n.tr("are you sure?"), JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
          dispose();
        }
      }
    });

    sendBtn.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent e)
      {
        if (isCorrect()) {
          GitHubClient client = new GitHubClient();
          String userToken    = GitHubAuth.getToken();
          client.setOAuth2Token(userToken != null ? userToken : anonymousToken());
          Issue issue = new Issue();
          issue.setTitle(title.getText());
          issue.setBody(feedback.getText());
          IssueService issueService = new IssueService(client);
          try {
            Issue i = issueService.createIssue("BuggleInc", "PLM", issue);
            JOptionPane.showMessageDialog(sendBtn,
                                          Game.i18n.tr("Thank you for your remark, we will do our best to integrate it.\n"
                                                           + "Follow our progress at {0}.",
                                                       i.getHtmlUrl()),
                                          Game.i18n.tr("Thanks for your suggestion"), JOptionPane.INFORMATION_MESSAGE);
            dispose();
          } catch (IOException ex) {
            StringBuffer ctn = new StringBuffer(ex.getLocalizedMessage() + "\n");
            for (StackTraceElement elm : ex.getStackTrace()) {
              ctn.append(elm.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(cancelBtn, ctn.toString(), Game.i18n.tr("Error while uploading your feedback"), JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
          }
        } else {
          JOptionPane.showMessageDialog(FeedbackDialog.this,
                                        Game.i18n.tr("Your feedback needs some little changes before being send,\nplease fix the following issue(s):\n\n") +
                                            errorMsg,
                                        Game.i18n.tr("Incorrect feedback"), JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    JPanel toolbar = new JPanel();
    toolbar.add(cancelBtn);
    toolbar.add(connectBtn);
    toolbar.add(sendBtn);
    add(toolbar, BorderLayout.SOUTH);

    addWindowFocusListener(new WindowAdapter() {
      @Override public void windowGainedFocus(WindowEvent e)
      {
        refreshForm();
      }
    });

    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    pack();
    setMinimumSize(new Dimension(200, 600));
    setPreferredSize(new Dimension(500, 800));
    setResizable(true);

    setLocationRelativeTo(MainFrame.getInstance());
  }

  private void selectCategory(Category c)
  {
    category      = c;
    String prefix = title.getText().equals(defaultTitle) ? "" : TITLE_PREFIX.matcher(title.getText()).replaceFirst("");
    title.setText(c.prefix + prefix);
    refreshForm();
    categoryPanel.setVisible(!sendBtn.isEnabled()); // Keep the choice open if this category cannot be sent
  }

  /**
   * Adapts the form to the selected category and to the GitHub identity: the buttons are labeled accordingly and the text is regenerated as long
   * as the user did not edit it. The public user ID is only sent by anonymous feedback, so it is removed once the user is logged in.
   */
  private void refreshForm()
  {
    String login = GitHubAuth.getLogin();
    connectBtn.setVisible(login == null);
    sendBtn.setText(login == null ? Game.i18n.tr("Send anonymously") : Game.i18n.tr("Send as @{0}", login));
    sendBtn.setEnabled(category != null && (category != Category.QUESTION || login != null));

    boolean untouched = feedback.getText().equals(lastBody);
    String text       = feedback.getText();
    if (untouched)
      text = (category == null ? "" : category.template(login != null) + "\n") + techInfo;
    if (login != null)
      text = ID_LINE.matcher(text).replaceAll("");
    if (!text.equals(feedback.getText()))
      feedback.setText(text);
    if (untouched)
      lastBody = feedback.getText();
  }

  /** The token of the account used to report the issues of the users that are not logged in GitHub. */
  private static String anonymousToken()
  {
    String rot13       = "apa3a7aqana0a5ara6asana8a7aoa1ana9a2aqa1a5a9a2aoa2a5a2ara5aqaqa8apa8a1a4a4apaoa3";
    StringBuffer token = new StringBuffer();
    for (int i = 1; i < rot13.length(); i += 2) {
      char c = rot13.charAt(i);
      if (c >= 'a' && c <= 'm')
        c += 13;
      else if (c >= 'A' && c <= 'M')
        c += 13;
      else if (c >= 'n' && c <= 'z')
        c -= 13;
      else if (c >= 'N' && c <= 'Z')
        c -= 13;
      else if (c >= '0' && c <= '4')
        c += 5;
      else if (c >= '5' && c <= '9')
        c -= 5;
      token.append(c);
    }
    return token.toString();
  }

  public boolean isCorrect()
  {
    boolean correct  = true;
    StringBuffer msg = new StringBuffer();
    String subject   = TITLE_PREFIX.matcher(title.getText()).replaceFirst("");
    if (subject.equals(defaultTitle)) {
      correct = false;
      msg.append(Game.i18n.tr("The feedback's title is still the default one, please specify a relevant one.\n"));
    } else if (subject.isEmpty()) {
      correct = false;
      msg.append(Game.i18n.tr("The current title is empty, please specify a relevant title.\n"));
    }
    if (feedback.getText().equals(lastBody)) {
      correct = false;
      msg.append(Game.i18n.tr("The feedback still contains the explanatory text (above the line of ---------), please remove it.\n"));
    }
    errorMsg = msg.toString();
    return correct;
  }

  /** The kinds of reports. Each one has a prefix for the issue title and a text guiding the user. */
  private enum Category {
    BUG("[Bug] "),
    WORDING("[Wording] "),
    QUESTION("[Question] "),
    OTHER("[Other] ");

    final String prefix;

    Category(String prefix) { this.prefix = prefix; }

    String label()
    {
      return switch (this) {
        case BUG -> Game.i18n.tr("I want to report a technical problem");
        case WORDING -> Game.i18n.tr("I found a typo, or I want to suggest a better wording of the instructions");
        case QUESTION -> Game.i18n.tr("I did not understand the instructions and I want to ask a question");
        case OTHER -> Game.i18n.tr("Something else");
      };
    }

    String template(boolean loggedIn)
    {
      return switch (this) {
        case BUG -> Game.i18n.tr("What I did:\n\n\n\nWhat I expected to happen:\n\n\n\nWhat happened instead:\n\n\n\n");
        case WORDING -> Game.i18n.tr("Where (lesson, exercise, sentence):\n\n\n\nCurrent text:\n\n\n\nWhat is wrong or unclear:\n\n\n\nMy proposal:\n\n\n\n");
        case QUESTION ->
          loggedIn ? Game.i18n.tr("My question, with all necessary details:\n\n\n\n\n")
                   : Game.i18n.tr("Questions cannot be answered through this window, so please do not send them here.\n"
                                  + "Come and ask on our Discord server instead: https://discord.gg/agxNPGj\n\n");
        case OTHER -> Game.i18n.tr("My report, with all necessary details:\n\n\n\n\n");
      };
    }
  }
}
