package plm.core.ui;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.GameListener;
import plm.core.ProgLangChangesListener;
import plm.core.model.Game;
import plm.core.model.ProgrammingLanguage;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;
import plm.core.model.session.SourceFile;
import plm.core.utils.PlmSyntaxPane;
import plm.universe.IEntityStackListener;
import plm.universe.World;



public class MissionEditorTabs extends JTabbedPane implements GameListener, ProgLangChangesListener {
	private static final long serialVersionUID = 1L;

	private Game game;
	private JEditorPane missionTab = new JEditorPane("text/html", "");
	
	/* for code tabs */
	private Lecture currentExercise;

	public I18n i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);
	
	public MissionEditorTabs() {
		super();
		
		/* Setup the mission tab */
		missionTab.setEditable(false);
		missionTab.setEditorKit(new PlmHtmlEditorKit());

		missionTab.addHyperlinkListener(new HyperlinkListener() {
			TipsDialog tipsDialog = null;
			
			@Override
			public void hyperlinkUpdate(HyperlinkEvent event) {
				if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					String desc = event.getDescription();
					if (desc.startsWith("#tip-")) {
						if (this.tipsDialog == null) {
							this.tipsDialog = new TipsDialog(MainFrame.getInstance());
						}
						this.tipsDialog.setText("<html>\n"+Lecture.HTMLTipHeader+"<body>\n"+currentExercise.getTip(desc)+"</body>\n</html>\n");
						this.tipsDialog.setVisible(true);
					}
					if (desc.startsWith("plm://")) {
						//Load a regular lesson
						String lessonName = desc.substring(new String("plm://").length());
						String exoName = null;
						int sep = lessonName.indexOf("/");
						if (sep != -1) {
							exoName = lessonName.substring(sep+1);
							lessonName = lessonName.substring(0, sep);
							if (exoName.length()==0)
								exoName = null;
						}
						if (Game.getInstance().isDebugEnabled()) 
							System.out.println("Following a link to lesson: "+lessonName+( (exoName != null) ? "; exo: "+exoName : " (no exo specified)"));
								
						Lesson lesson = Game.getInstance().switchLesson(lessonName,false);
						Game.getInstance().setCurrentLesson(lesson);
						if (exoName != null && exoName.length()>0) {
							Lecture lect = lesson.getExercise(exoName);
							if (lect != null) {
								Game.getInstance().setCurrentExercise(lect);
							} else {
								System.err.println("Broken link: no such lecture '"+exoName+"' in lesson "+lessonName);
							}
						}					 
					}
				}
			}
		});
		
		this.addTab(i18n.tr("Mission"), null, new JScrollPane(missionTab),
				i18n.tr("Description of the work to do"));
		
		PlmSyntaxPane.initKits();

		/* Register to game engine */
		this.game = Game.getInstance();
		this.game.addGameListener(this);
		this.game.addProgLangListener(this);
		
		/* add code tabs if the initialization is done already */
		if (game.getCurrentLesson() != null)
			currentExerciseHasChanged(game.getCurrentLesson().getCurrentExercise());
		
		/* removes keybindings from the JTextField
		 * Used to permit CTRL-PageUp and CTR-PageDown to change tabs */
		
//		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ctrl pressed PAGE_DOWN"), null );
//		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ctrl pressed PAGE_UP" ), null );
//		this.missionTab.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN,InputEvent.CTRL_DOWN_MASK ), null );
//		this.missionTab.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,InputEvent.CTRL_DOWN_MASK ), null );
//		System.out.println(showKeys(this, "MissionEditorTabs"));
//		System.out.println(showKeys(missionTab, "JEditorPane"));
	}
	
	public static String showKeys(JComponent jc, String nom) {
		String res="";
		res+=nom+" : "; 
		
		KeyStroke[] tab = jc.getInputMap().allKeys();
		for (int i = 0; i < tab.length-1; i++) {
			res+=tab[i].toString()+" , ";
			if(tab[i].toString().contains("PAGE_UP")||tab[i].toString().contains("PAGE_DOWN"))
				System.err.println(tab[i].toString());
		}
		res+=tab[tab.length-1].toString()+"";
		return res;
	}
	@Override
	public void currentExerciseHasChanged(Lecture lecture) {
		currentExercise = lecture;		

		currentProgrammingLanguageHasChanged(Game.getProgrammingLanguage()); /* Redo any code panel, and reload the mission */
		selectedEntityHasChanged();
		doLayout();
	}
	@Override
	public void selectedWorldHasChanged(World w) { 
		selectedEntityHasChanged();
	}
	
	@Override
	public void currentProgrammingLanguageHasChanged(ProgrammingLanguage newLang) { /* Redo any code panel */
		int tabPosition = getSelectedIndex();
		/* Remove every tabs, but the mission one */
		while (getTabCount()>1) {
			IEditorPanel p = (IEditorPanel) this.getComponentAt(getTabCount()-1);
			p.clear();
			removeTabAt(getTabCount()-1);
		}

		if (currentExercise instanceof Exercise) {
			/* Add back the right amount of tabs */
			int publicSrcFileCount = ((Exercise) currentExercise).getSourceFileCount(newLang);
			for (int i = 0; i < publicSrcFileCount; i++) {
				/* Create the code editor */
				SourceFile srcFile = ((Exercise) currentExercise).getSourceFile(newLang, i);

				/* Create the tab with the code editor as content */
				this.addTab(srcFile.getName(), null, srcFile.getEditorPanel(newLang), i18n.tr("Type your code here")); 
			}		
			if (getTabCount()>tabPosition)
				setSelectedIndex(tabPosition);
		}
		/* Change the mission text, because the CSS changed */
		missionTab.setEditorKit(new PlmHtmlEditorKit(game.getCurrentLesson().getCurrentExercise()));
		missionTab.setText(this.game.getCurrentLesson().getCurrentExercise().getMission(newLang));
		missionTab.setCaretPosition(0);
	}

	@Override
	public void selectedEntityHasChanged() { /* the code panels may want to know */
		for (int i=1;i<getTabCount();i++) {
			Component c = this.getComponentAt(getTabCount()-1);
			if (c instanceof IEntityStackListener)
				((IEntityStackListener) c).tracedEntityChanged(game.getSelectedEntity());
		}
	}
	@Override
	public void selectedWorldWasUpdated() { /* don't care */ }
}
