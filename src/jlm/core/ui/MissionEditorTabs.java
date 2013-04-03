package jlm.core.ui;

import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.petebevin.markdown.MarkdownProcessor;

import jlm.core.GameListener;
import jlm.core.ProgLangChangesListener;
import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lecture;
import jlm.core.model.lesson.Lesson;
import jlm.core.model.lesson.SourceFile;
import jlm.universe.IEntityStackListener;
import jlm.universe.World;
import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.SyntaxStyle;
import jsyntaxpane.SyntaxStyles;
import jsyntaxpane.TokenType;

public class MissionEditorTabs extends JTabbedPane implements GameListener, ProgLangChangesListener {
	private static final long serialVersionUID = 1L;

	private Game game;
	private JEditorPane missionTab = new JEditorPane();
	MarkdownProcessor markdownProcessor;
	MarkdownEditorView editeur;
	String path_md;

	/* for code tabs */
	private Lecture currentExercise;
	//	private Font font = null;


	public MissionEditorTabs() {
		super();
		path_md = (Game.getInstance().getCurrentLesson().getCurrentExercise()+"").replace('.', '/').replaceAll("@.*", "");
		init();

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
					if (desc.startsWith("jlm://")) {
						//Load a regular lesson
						String lessonName = desc.substring(new String("jlm://").length());
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
								
						Lesson lesson = Game.getInstance().switchLesson(lessonName);
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
		
		this.addTab("Mission", null, new JScrollPane(missionTab),
				"Description of the work to do");
		
		/* setup code tabs */
		DefaultSyntaxKit.initKit();
		loadFont();
		configureSyntaxStyles();

		/* Register to game engine */
		this.game = Game.getInstance();
		this.game.addGameListener(this);
		this.game.addProgLangListener(this);

		/* add code tabs */
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

		this.game.getCurrentLesson().getCurrentExercise().getMission(Game.getProgrammingLanguage());
		init();
		currentProgrammingLanguageHasChanged(Game.getProgrammingLanguage());
		selectedEntityHasChanged();
		doLayout();
	}
	@Override
	public void currentLessonHasChanged() { /* don't care */ }
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
			int publicSrcFileCount = ((Exercise) currentExercise).sourceFileCount(newLang);
			for (int i = 0; i < publicSrcFileCount; i++) {
				/* Create the code editor */
				SourceFile srcFile = ((Exercise) currentExercise).getPublicSourceFile(newLang, i);

				/* Create the tab with the code editor as content */
				this.addTab(srcFile.getName(), null, srcFile.getEditorPanel(newLang), "Type your code here"); 
			}		
			if (getTabCount()>tabPosition)
				setSelectedIndex(tabPosition);
		}

		/* Change the mission text, because the CSS changed */
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

	/* setup methods */
	private void configureSyntaxStyles() {
		//TODO: can be configured through a property file in the new version of jsyntaxpane
		SyntaxStyles st = SyntaxStyles.getInstance();
		st.put(TokenType.OPERATOR, new SyntaxStyle(Color.BLACK, false, false)); // black
		st.put(TokenType.KEYWORD, new SyntaxStyle(new Color(0x8d0056), true, false)); // violet,
		// bold
		st.put(TokenType.TYPE, new SyntaxStyle(Color.BLACK, false, false)); // black
		st.put(TokenType.COMMENT, new SyntaxStyle(new Color(0x29825e), false, false)); // dark
		// green
		st.put(TokenType.NUMBER, new SyntaxStyle(Color.BLACK, false, false)); // black
		// st.add(TokenType.REGEX, new SyntaxStyle(new Color(0xcc6600), false, false)); // not used in Java
		// st.add(TokenType.IDENT, new SyntaxStyle(new Color(0x1300c5), false, false)); // dark blue
		st.put(TokenType.IDENTIFIER, new SyntaxStyle(Color.black, false, false)); // black
		st.put(TokenType.STRING, new SyntaxStyle(new Color(0x3600ff), false, false)); // blue
		st.put(TokenType.DEFAULT, new SyntaxStyle(Color.BLACK, false, false)); // black
	}

	private void loadFont() {
		/*
		 * Font f = new Font("Monaco", Font.PLAIN, 12); 
		 * if (f != null) {
		 * editor.setFont(f); 
		 * }
		 */


		/*	InputStream is = null;
		try {
			// is = getClass().getResourceAsStream("/resources/Monaco.ttf");
			// FIXME: must try this font on neptune/linux/windows ? it works
			// well on osx
			is = getClass().getResourceAsStream("/resources/Envy.Code.R.ttf");

			if (is == null) // give it another try 
				is = getClass().getResourceAsStream("/Envy.Code.R.ttf");
			if (is == null)	// really not found	
				throw new IOException("font file not found");

			f = Font.createFont(Font.TRUETYPE_FONT, is);
			f = f.deriveFont((float) 12);
		} catch (FontFormatException ffe) {
			// ffe.printStackTrace(); Yeah, that's kinda expected...
			f = null;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			f = null;
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}	
		 */	
	}

	public void init() {
		this.removeAll();
		markdownProcessor = new MarkdownProcessor();
		MarkdownDocument md_doc = new MarkdownDocument(path_md);
		editeur = new MarkdownEditorView(md_doc);

		editeur.apercu.addHyperlinkListener(new HyperlinkListener() {
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

					if (desc.startsWith("jlm://")) {
						if (desc.startsWith("jlm://load_jar")) { 
							//Load a lesson JAR
							JFileChooser fc = new JFileChooser();
							fc.setFileFilter(new FileNameExtensionFilter("JAR filter", "jar"));
							fc.setDialogType(JFileChooser.OPEN_DIALOG);
							fc.showOpenDialog(MainFrame.getInstance());
							File selectedFile = fc.getSelectedFile();

							try {
								if (selectedFile != null)
									game.loadLessonFromJAR(fc.getSelectedFile());
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							//Load a regular lesson
							String lessonName = desc.substring(new String("jlm://").length());
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

							Lesson lesson = Game.getInstance().switchLesson(lessonName);
							Game.getInstance().setCurrentLesson(lesson);

							if (exoName != null && exoName.length()>0) {
								Lecture lect = lesson.getExercise(exoName);
								if (lect != null) {
									Game.getInstance().setCurrentExercise(lect);
								} else {
									System.err.println("Broken link: no such lecture '"+exoName+"' in lesson "+lessonName);
								}
							}

							path_md = (lessonName.replace('.', '/').replaceAll("@.*", ""))+"/Main";
							MarkdownDocument md_doc = new MarkdownDocument(path_md);
							editeur.apercu.setText(markdownProcessor.markdown(md_doc.getTexte()));
						}

					}
				}
			}
		});

		if(Global.admin){
			this.addTab("Mission", null, editeur,
					"Description of the work to do");
		}
		else{
			this.addTab("Mission", null, new JScrollPane(editeur.apercu),
					"Description of the work to do");
		}
		
		this.repaint();
		this.validate();
	}
}
