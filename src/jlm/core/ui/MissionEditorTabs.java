package jlm.core.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLEditorKit;

import com.petebevin.markdown.MarkdownProcessor;

import jlm.core.GameListener;
import jlm.core.ProgLangChangesListener;
import jlm.core.model.Game;
import jlm.core.JLMClassLoader;
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

import java.lang.reflect.Method;


public class MissionEditorTabs extends JTabbedPane implements GameListener, ProgLangChangesListener {
	private static final long serialVersionUID = 1L;

	private Game game;
	private JEditorPane missionTab = new JEditorPane();

	/* for code tabs */
	private Lecture currentExercise;
	//	private Font font = null;


	public MissionEditorTabs() {
		super();
		maj();
	}

	@Override
	public void currentExerciseHasChanged(Lecture lecture) {
		currentExercise = lecture;		

		currentProgrammingLanguageHasChanged(Game.getProgrammingLanguage()); /* Redo any code panel, and reload the mission */
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
			int publicSrcFileCount = ((Exercise) currentExercise).publicSourceFileCount(newLang);
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

	public void maj() {
		this.removeAll();
		if(Global.admin){
			System.err.println(1);
			MarkdownDocument md_doc = new MarkdownDocument("/Users/bogy/Desktop/test.md");

			System.err.println(11);
			MarkdownEditorView editeur = new MarkdownEditorView(md_doc);

			System.err.println(2);
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
										JLMClassLoader.addJar(fc.getSelectedFile());
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

								Lesson lesson = Game.getInstance().loadLesson(lessonName);
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
				}
			});

			System.err.println(3);
			this.addTab("Mission", null, editeur,
					"Description of the work to do");

			System.err.println(4);
		}
		else{
			MarkdownProcessor markdownProcessor = new MarkdownProcessor();
			JTextPane apercu;
			Dimension dimension_prefered = new Dimension(490, 520);
			Dimension dimension_minimum = new Dimension(10, 10);
			MarkdownDocument md_doc = new MarkdownDocument("/Users/bogy/Desktop/test.md");
			
			apercu = new JTextPane();
			apercu.setEditorKit(new HTMLEditorKit());
			apercu.setText(markdownProcessor.markdown(md_doc.getTexte()));
			apercu.setEditable(false);
	        JScrollPane preview_scroll_pane = new JScrollPane(apercu);
	        preview_scroll_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        preview_scroll_pane.setPreferredSize(dimension_prefered);
	        preview_scroll_pane.setMinimumSize(dimension_minimum);
	        
	        apercu.addHyperlinkListener(new HyperlinkListener() {
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
										JLMClassLoader.addJar(fc.getSelectedFile());
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

								Lesson lesson = Game.getInstance().loadLesson(lessonName);
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
				}
			});

			this.addTab("Mission", null, new JScrollPane(apercu),
					"Description of the work to do");
		}

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
		this.repaint();
		this.validate();
	}
}
