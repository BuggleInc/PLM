package jlm.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import jlm.core.Game;
import jlm.event.GameListener;
import jlm.lesson.Exercise;
import jlm.lesson.SourceFile;
import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.SyntaxStyle;
import jsyntaxpane.SyntaxStyles;
import jsyntaxpane.TokenType;

public class CodePanel extends JTabbedPane implements GameListener {
// TODO: clean up this class. It's a mess!
	
	
	private static final long serialVersionUID = 3242655852676336111L;
	private Game game;
	private Exercise currentExercise;

	// private EditorKit editorKit;

	private ArrayList<JEditorPane> codeEditors = new ArrayList<JEditorPane>();
	private ArrayList<SourceFile> sourceFiles = new ArrayList<SourceFile>();
	private ArrayList<SourceFileDocumentSynchronizer> synchronizers = new ArrayList<SourceFileDocumentSynchronizer>();

	public CodePanel(Game game) {
		super(JTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT);
		this.game = game;
		this.game.addGameListener(this);

		// this.editorKit = new SyntaxKit("java");
		// this.editorKit = SyntaxKitFactory.getKitForLanguage("java");

		DefaultSyntaxKit.initKit();

		loadFont();
		configureSyntaxStyles();
		currentExerciseHasChanged();
	}

	protected void updateComponents() {
		int publicSrcFileCount = this.currentExercise.publicSourceFileCount();

		// if we have to remove few tabs
		for (int i = this.getTabCount() - 1; i >= publicSrcFileCount; i--) {
			this.removeTabAt(i);
			JEditorPane codeEditor = this.codeEditors.remove(i);
			SourceFileDocumentSynchronizer sync = this.synchronizers.remove(i);
			SourceFile srcFile = this.sourceFiles.remove(i);

			codeEditor.getDocument().removeDocumentListener(sync);
			sync.clear();
			srcFile.setListener(null);
		}
		// if we have to add few tabs
		for (int i = this.getTabCount(); i < publicSrcFileCount; i++) {			
			JEditorPane codeEditor = createJavaEditorPane();
			JScrollPane scrPane = new JScrollPane(codeEditor);
			codeEditor.setContentType("text/java");

			if (f != null) {
				codeEditor.setFont(f);
			}

			
			
			this.codeEditors.add(i, codeEditor);
			// this.synchronizers.add(i, new
			// SourceFileDocumentSynchronizer(this.editorKit));
			this.synchronizers.add(i, new SourceFileDocumentSynchronizer(codeEditor.getEditorKit()));
			//codeEditor.setContentType("text/java");
			// remove auto-indent when return is pressed
			//codeEditor.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke("ENTER"));
			//codeEditor.setEditable(true);

			// TODO: to be remove when reported issue 47 in JSyntaxPane is resolved.
			//scrPane.setRowHeaderView(new LineNumberMarginPanel(codeEditor));

			this.addTab("tmp" + i, scrPane);
		}

		for (SourceFile srcFile : this.sourceFiles) {
			srcFile.setListener(null);
		}
		this.sourceFiles.clear();
				
		for (int i = 0; i < publicSrcFileCount; i++) {
			SourceFile srcFile = this.currentExercise.getPublicSourceFile(i);
			this.sourceFiles.add(i, srcFile);

			this.setTitleAt(i, srcFile.getName() + ".code");
			JEditorPane codeEditor = this.codeEditors.get(i);

			SourceFileDocumentSynchronizer sync = this.synchronizers.get(i);
			srcFile.setListener(sync);
			sync.setDocument(codeEditor.getDocument());
			sync.setSourceFile(srcFile);
			codeEditor.getDocument().addDocumentListener(sync);

			//codeEditor.setContentType("text/plain");
			codeEditor.setText(srcFile.getBody());			
			//codeEditor.setContentType("text/java");
		}
		
		//this.doLayout();
		//for (int i = 0; i < publicSrcFileCount; i++) {
		//	this.codeEditors.get(i).setContentType("text/java");
		//}

	}

	@Override
	public void currentExerciseHasChanged() {
		this.currentExercise = this.game.getCurrentLesson().getCurrentExercise();		
		updateComponents();
	}

	@Override
	public void currentLessonHasChanged() {
		// don't care
	}

	@Override
	public void lessonsChanged() {
		// don't care
	}

	@Override
	public void selectedWorldHasChanged() {
		// don't care
	}

	@Override
	public void selectedEntityHasChanged() {
		// don't care
	}

	@Override
	public void selectedWorldWasUpdated() {
		// don't care
	}

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
		// st.add(TokenType.REGEX, new SyntaxStyle(new Color(0xcc6600), false,
		// false)); // not used in Java
		// st.add(TokenType.IDENT, new SyntaxStyle(new Color(0x1300c5), false,
		// false)); // dark blue
		st.put(TokenType.IDENTIFIER, new SyntaxStyle(Color.black, false, false)); // black
		st.put(TokenType.STRING, new SyntaxStyle(new Color(0x3600ff), false, false)); // blue
		st.put(TokenType.DEFAULT, new SyntaxStyle(Color.BLACK, false, false)); // black

	}

	
	Font f = null;
	private void loadFont() {
		/*
		 * Font f = new Font("Monaco", Font.PLAIN, 12); 
		 * if (f != null) {
		 * editor.setFont(f); 
		 * }
		 */

		
	/*	
		//Font f = null;
		InputStream is = null;
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
	
	private JEditorPane createJavaEditorPane() {
		JEditorPane editor = new JEditorPane();
		// FIXME: does not work well on neptune ;-(
		// editor.setEditorKit(editorKit);
		// editor.setContentType("text/java");
		if (f != null) {
			editor.setFont(f);
		}

		// remove auto-indent when return is pressed
		// editor.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke("ENTER"));
		// editor.setEditable(true);

		
		return editor;
	}

}
