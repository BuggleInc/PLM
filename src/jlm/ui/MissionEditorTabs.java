package jlm.ui;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;

import jlm.core.Game;
import jlm.event.GameListener;
import jlm.lesson.Exercise;
import jlm.lesson.SourceFile;
import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.SyntaxStyle;
import jsyntaxpane.SyntaxStyles;
import jsyntaxpane.TokenType;

public class MissionEditorTabs extends JTabbedPane implements GameListener {
	private static final long serialVersionUID = 1L;

	private Game game;
	private JEditorPane missionTab = new JEditorPane("text/html", "");
	
	/* for code tabs */
	private Exercise currentExercise;
	private ArrayList<SourceFile> sourceFiles = new ArrayList<SourceFile>();
	private Font font = null;

	
	public MissionEditorTabs() {
		super();
		
		/* Setup the mission tab */
		missionTab.setEditable(false);
		missionTab.setEditorKit(new JlmHtmlEditorKit());
		StyleSheet styles = ((HTMLDocument) missionTab.getDocument()).getStyleSheet();
		styles.importStyleSheet(getClass().getResource("/lessons/screen.css"));			

		this.add("Mission", new JScrollPane(missionTab));
		
		/* setup code tabs */
		DefaultSyntaxKit.initKit();
		loadFont();
		configureSyntaxStyles();

		/* Register to game engine */
		this.game = Game.getInstance();
		this.game.addGameListener(this);
		
		/* load content */
		currentExerciseHasChanged();
	}
	
	@Override
	public void currentExerciseHasChanged() {
		currentExercise = game.getCurrentLesson().getCurrentExercise();		
		
		/* Change the mission text */
		missionTab.setText(this.game.getCurrentLesson().getCurrentExercise().getMission());
		missionTab.setCaretPosition(0);
		
		/* Redo any code panel */
		int publicSrcFileCount = currentExercise.publicSourceFileCount();

		/* notify all previously edited source files that they are not under edit anymore */
		for (SourceFile srcFile : sourceFiles) 
			srcFile.removeListener();		
		sourceFiles.clear();
		
		/* Remove every tabs, but the mission one */
		while (getTabCount()>1)
			this.remove(getTabCount()-1);

		/* Add back the right amount of tabs */
		for (int i = 0; i < publicSrcFileCount; i++) {
			/* Create the code editor */
			JEditorPane codeEditor = new JEditorPane();
			JScrollPane scrollPane = new JScrollPane(codeEditor);
			codeEditor.setContentType("text/java");
			if (font != null) 
				codeEditor.setFont(font);
			
			/* Create a synchronization element, and connect it to the editor */
			SourceFileDocumentSynchronizer sync = new SourceFileDocumentSynchronizer(codeEditor.getEditorKit());
			sync.setDocument(codeEditor.getDocument());
			codeEditor.getDocument().addDocumentListener(sync);
			
			/* Connect the synchronization element to the source file */
			SourceFile srcFile = currentExercise.getPublicSourceFile(i);
			sourceFiles.add(i, srcFile);
			srcFile.setListener(sync);
			sync.setSourceFile(srcFile);
			
			codeEditor.setText(srcFile.getBody());
			
			/* Create the tab with the code editor as content */
			this.addTab(srcFile.getName(), scrollPane); // name is over written in next loop			
		}		
	}
	@Override
	public void currentLessonHasChanged() { /* don't care */ }
	@Override
	public void lessonsChanged() { /* don't care */ }
	@Override
	public void selectedWorldHasChanged() { /* don't care */ }
	@Override
	public void selectedEntityHasChanged() { /* don't care */ }
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

}
