package plm.core.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.function.Supplier;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import plm.core.GameListener;
import plm.core.ProgLangChangesListener;
import plm.core.UserSwitchesListener;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.User;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.universe.IEntityStackListener;
import plm.universe.World;

public class MissionEditorTabs extends JTabbedPane implements GameListener, ProgLangChangesListener, UserSwitchesListener {
  private static final long serialVersionUID = 1L;

  private Game game;
  private JEditorPane missionTab = new JEditorPane("text/html", "");

  /* for code tabs */
  private Lecture currentExercise;

  public MissionEditorTabs()
  {
    super();

    /* Setup the mission tab */
    missionTab.setEditable(false);
    missionTab.setEditorKit(new PlmHtmlEditorKit());

    missionTab.addHyperlinkListener(new HyperlinkListener() {
      TipsDialog tipsDialog = null;

      @Override public void hyperlinkUpdate(HyperlinkEvent event)
      {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
          String desc = event.getDescription();
          if (desc.startsWith("#tip-")) {
            if (this.tipsDialog == null) {
              this.tipsDialog = new TipsDialog(MainFrame.getInstance());
            }
            this.tipsDialog.setText("<html>\n" + Lecture.HTMLTipHeader + "<body>\n" + currentExercise.getTip(desc) + "</body>\n</html>\n");
            this.tipsDialog.setVisible(true);
            ProgrammingLanguage lang = Game.getInstance().getProgrammingLanguage();
            String mission           = currentExercise.getMission(lang);
            Game.getInstance().getGitTracker().readTip(desc, mission, lang);
          }
          if (desc.startsWith("plm://")) {
            // Load a regular lesson
            Game.getInstance().setCurrentExercise(desc);
          }
        }
      }
    });

    this.addTab(Game.i18n.tr("Mission"), null, new JScrollPane(missionTab), Game.i18n.tr("Description of the work to do"));

    // PlmSyntaxPane.initKits();

    /* Register to game engine */
    this.game = Game.getInstance();
    this.game.addGameListener(this);
    this.game.addProgLangListener(this);
    this.game.getUsers().addUserSwitchesListener(this);

    /* add code tabs if the initialization is done already */
    if (game.getCurrentLesson() != null)
      currentExerciseHasChanged(game.getCurrentLesson().getCurrentExercise());

    /* removes keybindings from the JTextField
     * Used to permit CTRL-PageUp and CTR-PageDown to change tabs */

    //		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ctrl pressed PAGE_DOWN"), null );
    //		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ctrl pressed PAGE_UP" ), null );
    //		this.missionTab.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN,InputEvent.CTRL_DOWN_MASK
    //), null );
    //		this.missionTab.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,InputEvent.CTRL_DOWN_MASK
    //), null ); 		System.out.println(showKeys(this, "MissionEditorTabs")); 		System.out.println(showKeys(missionTab, "JEditorPane"));
  }

  public static String showKeys(JComponent jc, String nom)
  {
    String res = "";
    res += nom + " : ";

    KeyStroke[] tab = jc.getInputMap().allKeys();
    for (int i = 0; i < tab.length - 1; i++) {
      res += tab[i].toString() + " , ";
      if (tab[i].toString().contains("PAGE_UP") || tab[i].toString().contains("PAGE_DOWN"))
        System.err.println(tab[i].toString());
    }
    res += tab[tab.length - 1].toString() + "";
    return res;
  }
  @Override public void currentExerciseHasChanged(Lecture lecture)
  {
    currentExercise = lecture;

    currentProgrammingLanguageHasChanged(Game.getInstance().getProgrammingLanguage()); /* Redo any code panel, and reload the mission */
    selectedEntityHasChanged();
    doLayout();
  }
  @Override public void selectedWorldHasChanged(World w) { selectedEntityHasChanged(); }

  @Override public void currentProgrammingLanguageHasChanged(ProgrammingLanguage newLang)
  { /* Redo any code panel */
    int tabPosition = getSelectedIndex();
    /* Remove every tabs, but the mission one: the code editors, and the read-only tabs of the previous world */
    while (getTabCount() > 1) {
      Component tab = this.getComponentAt(getTabCount() - 1);
      if (tab instanceof IEditorPanel editor)
        editor.clear();
      else if (tab instanceof ReadOnlyTab readOnly)
        readOnly.dispose();
      removeTabAt(getTabCount() - 1);
    }

    if (currentExercise instanceof Exercise) {
      /* Add back the code editor, if there is a source file in that language */
      Exercise exo = (Exercise)currentExercise;
      if (exo.getSourceFileCount(newLang) > 0)
        this.addTab(exo.getTabName(), null, exo.getEditorPanel(newLang), Game.i18n.tr("Type your code here"));
      if (getTabCount() > tabPosition)
        setSelectedIndex(tabPosition);
    }
    /* Change the mission text, because the CSS changed */
    missionTab.setEditorKit(new PlmHtmlEditorKit(game.getCurrentLesson().getCurrentExercise()));
    missionTab.setText(this.game.getCurrentLesson().getCurrentExercise().getMission(newLang));
    missionTab.setCaretPosition(0);
  }

  /**
   * Shows that text of the world in a read-only tab, which the student can close. Its title is followed by the name of the world, and
   * by "objective" for the expected world, since the student may open the same text in several worlds. A tab already open on that
   * text of that world is selected, and shows the given content from now on (a socket of a new execution, for example). The tab
   * follows the changes of the world, and is closed when the exercise or the language changes.
   */
  public void showReadOnly(String title, World world, Supplier<String> content)
  {
    title += " (" + world.getName() + (world == game.getAnswerOfSelectedWorld() ? ", " + Game.i18n.tr("objective") : "") + ")";
    for (int i = 1; i < getTabCount(); i++)
      if (getComponentAt(i) instanceof ReadOnlyTab open && open.world == world && getTitleAt(i).equals(title)) {
        open.show(content);
        setSelectedIndex(i);
        return;
      }
    ReadOnlyTab tab = new ReadOnlyTab(world, content);
    addTab(title, null, tab, Game.i18n.tr("Read-only view of the world"));

    JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
    header.setOpaque(false);
    header.add(new JLabel(title));
    JButton close = new JButton("×");
    close.setBorder(BorderFactory.createEmptyBorder());
    close.setContentAreaFilled(false);
    close.setToolTipText(Game.i18n.tr("Close this tab"));
    close.addActionListener(event -> {
      tab.dispose();
      remove(tab);
    });
    header.add(close);
    setTabComponentAt(indexOfComponent(tab), header);
    setSelectedComponent(tab);
  }

  @Override public void selectedEntityHasChanged()
  { /* the code panels may want to know */
    for (int i = 1; i < getTabCount(); i++) {
      Component c = this.getComponentAt(i);
      if (c instanceof IEntityStackListener)
        ((IEntityStackListener)c).tracedEntityChanged(game.getSelectedEntity());
    }
  }
  @Override public void selectedWorldWasUpdated() { /* don't care */ }

  @Override public void userHasChanged(User newUser) { currentProgrammingLanguageHasChanged(Game.getInstance().getProgrammingLanguage()); }
}
