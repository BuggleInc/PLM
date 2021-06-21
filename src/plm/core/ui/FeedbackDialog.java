package plm.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.tracking.GitUtils;
import plm.universe.World;

public class FeedbackDialog extends JDialog {

	private static final long serialVersionUID = 0;
	private static FeedbackDialog instance = null;
	private static String defaultTitle = "";
	private static String defaultText = "";	
	
	public I18n i18n = I18nFactory.getI18n(getClass(), "org.plm.i18n.Messages", getLocale(), I18nFactory.FALLBACK);
	public String errorMsg;
	final JEditorPane feedback = new JEditorPane();
	final JTextField title = new JTextField();
	
	public static FeedbackDialog getInstance() {
		if (FeedbackDialog.instance == null) {
			FeedbackDialog.instance = new FeedbackDialog();
		}
		StringBuffer worldInfo = new StringBuffer();
		for (World w:((Exercise)Game.getInstance().getCurrentLesson().getCurrentExercise()).getWorlds(WorldKind.ANSWER)) {
			String s = w.getDebugInfo();
			if (s != "") 
				worldInfo.append("World: "+s+"\n");
		}

		defaultTitle = FeedbackDialog.instance.i18n.tr("Please describe the problem in a few words");
		defaultText = FeedbackDialog.instance.i18n.tr(
				  "Your suggestion comes here, with all necessary details.\n\n\n\n\n\n"
				+ "(The following helps us fixing your problem, please don't erase)\n\n"
	            + "--------------------[ Technical Information ]--------------------\n"); /* The rest is not translated */
		
		Lecture exo = Game.getInstance().getCurrentLesson().getCurrentExercise();
		String proposedExo = "";
		if (exo instanceof Exercise)
			proposedExo = "\n"+FeedbackDialog.instance.i18n.tr("--------------------[ my code for this exercise ]--------------------\n")
			    + ((Exercise)exo).getSourceFile(Game.getProgrammingLanguage(), 0).getBody();

		FeedbackDialog.instance.feedback.setText(defaultText
				+ "Lesson: "+Game.getInstance().getCurrentLesson().getId() + "\n"
				+ "Exercise: "+Game.getInstance().getCurrentLesson().getCurrentExercise().getId() + "\n"
				+ worldInfo.toString()
				+ "Programming Language: "+Game.getProgrammingLanguage().getLang() + "\n"
				+ "Locale: "+Game.getInstance().getLocale().getDisplayName() + "\n"
				+ "Java version: " + System.getProperty("java.version") + " (VM: " + System.getProperty("java.vm.name") + "; version: " + System.getProperty("java.vm.version") + ")" + "\n"
				+ "OS: " + System.getProperty("os.name") + " (version: " + System.getProperty("os.version") + "; arch: " + System.getProperty("os.arch") + ")" + "\n"
				+ "PLM version: " + Game.getProperty("plm.major.version", "internal", false) + " (" + Game.getProperty("plm.minor.version", "internal", false) + ")" + "\n"
				+ "Public user ID: PLM"+GitUtils.sha1(Game.getInstance().getUsers().getCurrentUser().getUserUUIDasString())+ "\n"
				+ proposedExo);
		
		
		FeedbackDialog.instance.title.setText(defaultTitle);
		FeedbackDialog.instance.pack();
		return FeedbackDialog.instance;
	}

	private FeedbackDialog() {
		super(MainFrame.getInstance(), "Report your feedback", false);
		this.setTitle(i18n.tr("Report your feedback"));
		initComponent();
	}

	public void initComponent() {

		setLayout(new BorderLayout());
		JPanel headerToolbar = new JPanel();
		headerToolbar.add(new Label(i18n.tr("Issue title:")));
		headerToolbar.add(title);
		add(headerToolbar, BorderLayout.NORTH);
		
		JEditorPane hints = new JEditorPane("text/html","");
		hints.setEditorKit(new PlmHtmlEditorKit());
		hints.setEditable(false);
		hints.setText(i18n.tr(
			"<h1>Asking a question about the PLM</h1>\n"
			+"<p>If you're stuck or unsure, you can come and discuss with us on our <a href='https://discord.gg/agxNPGj'>Discord server</a>. "
			+"You can also use this channel to chat about new ideas to extend the PLM, or simply to chat with us with working with the PLM.</p>\n"

			+"<h1>Reporting a bug in PLM</h1>\n"
			+ "<p>If you have your own GitHub account, please head to the <a href='https://github.com/BuggleInc/PLM'>PLM Bug Tracker</a>. "
			+ "If not, you can use this window to report an issue in the PLM (be it in one exercise or in the PLM itself), "
			+ "but <b>don't ask questions in this window</b>, as we have no way to provide an answer on this channel. "
			+ "Please write your report in English or French if possible.</p>\n"
			+ "<p>You should include all relevant information that could help us fixing the issue. Don't remove the technical details unless you are certain that they are not relevant. "
			+ "What you write here will be public: <b>Never disclose passwords or other sensible information on a bug tracker</b></p>\n"
			+ "<p>When you find a typo or a sentence that is hard to understand, it really helps to suggest a new wording."
			+ "If you encounter a technical bug, please tell us what you did, which outcome you were expecting and what happened instead.</p><br/>"));
		hints.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent event) {
				if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					String uri = event.getDescription();

					try {
						if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
							System.out.print("Directly");
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
				
		add(hints, BorderLayout.NORTH);

		feedback.setBackground(Color.white);
		feedback.setOpaque(true);
		feedback.setEditable(true);
		JScrollPane jsp = new JScrollPane(feedback);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(jsp, BorderLayout.CENTER);

		feedback.setContentType("text/plain");

		final JButton cancelBtn = new JButton(i18n.tr("Cancel"));
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(cancelBtn,
																 i18n.tr("Do you really want to cancel your feedback and lose any edit?"),
																 i18n.tr("are you sure?"),
																 JOptionPane.YES_NO_OPTION);
				if (dialogResult == JOptionPane.YES_OPTION) {
					dispose();
				}
			}
		});

		final JButton sendBtn = new JButton(i18n.tr("Send feedback"));
		sendBtn.addActionListener(new ActionListener() {
			GitHubClient client = new GitHubClient();

			@Override
			public void actionPerformed(ActionEvent e) {
				if(isCorrect()) {
					String rot13 = "apa3a7aqana0a5ara6asana8a7aoa1ana9a2aqa1a5a9a2aoa2a5a2ara5aqaqa8apa8a1a4a4apaoa3";
					StringBuffer token = new StringBuffer();
					for (int i=1; i<rot13.length(); i+=2) {
			            char c = rot13.charAt(i);
			            if      (c >= 'a' && c <= 'm') c += 13;
			            else if (c >= 'A' && c <= 'M') c += 13;
			            else if (c >= 'n' && c <= 'z') c -= 13;
			            else if (c >= 'N' && c <= 'Z') c -= 13;
			            else if (c>= '0' && c <= '4') c +=5;
			            else if (c>= '5' && c <= '9') c -=5;
			            token.append(c);
					}
					client.setOAuth2Token(token.toString());
					Issue issue = new Issue();
					issue.setTitle(title.getText());
					issue.setBody(feedback.getText());
					IssueService issueService = new IssueService(client);
					try {
						Issue i = issueService.createIssue("BuggleInc", "PLM", issue);
						JOptionPane.showMessageDialog(sendBtn, i18n.tr(
								  "Thank you for your remark, we will do our best to integrate it.\n"
								+ "Follow our progress at {0}.",i.getHtmlUrl()), i18n.tr("Thanks for your suggestion"), JOptionPane.INFORMATION_MESSAGE);
						dispose();
					} catch (IOException ex) {
						StringBuffer ctn = new StringBuffer(ex.getLocalizedMessage() + "\n");
						for (StackTraceElement elm : ex.getStackTrace()) {
							ctn.append(elm.toString()).append("\n");
						}
						JOptionPane.showMessageDialog(cancelBtn,
													  ctn.toString(),
													  i18n.tr("Error while uploading your feedback"),
													  JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
				else {
					JOptionPane.showMessageDialog(FeedbackDialog.this, 
							i18n.tr("Your feedback needs some little changes before being send,\nplease fix the following issue(s):\n\n")+errorMsg, 
							i18n.tr("Incorrect feedback"), 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JPanel toolbar = new JPanel();
		toolbar.add(cancelBtn);
		toolbar.add(sendBtn);
		add(toolbar, BorderLayout.SOUTH);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
		setMinimumSize(new Dimension(200, 600));
		setPreferredSize(new Dimension(500, 800));
		setResizable(true);

		setLocationRelativeTo(getParent());
	}

	public boolean isCorrect() {
		boolean correct = true;
		StringBuffer msg = new StringBuffer();
		if(title.getText().equals(defaultTitle)) {
			correct = false;
			msg.append(i18n.tr("The feedback's title is still the default one, please specify a relevant one.\n"));
		}
		else if(title.getText().equals("")) {
			correct = false;
			msg.append(i18n.tr("The current title is empty, please specify a relevant title.\n"));
		}
		if(feedback.getText().contains(defaultText)) {
			correct = false;
			msg.append(i18n.tr("The feedback still contains the explanatory text (above the line of ---------), please remove it.\n"));
		}
		errorMsg = msg.toString();
		return correct;
	}
}
