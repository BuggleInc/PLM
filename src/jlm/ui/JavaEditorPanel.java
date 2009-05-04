package jlm.ui;

import java.awt.Color;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import jlm.core.Game;
import jlm.lesson.SourceFile;
import jlm.universe.Entity;
import jlm.universe.IEntityStackListener;

public class JavaEditorPanel extends JScrollPane implements IEditorPanel,IEntityStackListener {
	private static final long serialVersionUID = 1L;
	SourceFile srcFile;
	SourceFileDocumentSynchronizer sync ;
	JEditorPane codeEditor;
	Entity tracedEntity;

	static final Color DEFAULT_COLOR = new Color(230, 230, 210);

	// http://www.velocityreviews.com/forums/t147950-how-can-i-highlight-the-current-row-in-a-jtextarea.html
	private Highlighter.HighlightPainter painter;
	private Object highlight;
	
	public JavaEditorPanel(SourceFile srcFile) {
		super();
		this.srcFile = srcFile;

		codeEditor = new JEditorPane();
		setViewportView(codeEditor);
		codeEditor.setContentType("text/java");
		codeEditor.setCaretPosition(0);
		
		/* Create a synchronization element, and connect it to the editor */
		sync = new SourceFileDocumentSynchronizer(codeEditor.getEditorKit());
		sync.setDocument(codeEditor.getDocument());
		codeEditor.getDocument().addDocumentListener(sync);
		
		/* Connect the synchronization element to the source file */
		srcFile.setListener(sync);
		sync.setSourceFile(srcFile);
		
		codeEditor.setText(srcFile.getBody());
		
		/* Highlighting stuff, to trace entities */
		tracedEntity = Game.getInstance().getSelectedEntity();
		if (tracedEntity != null)
			tracedEntity.addStackListener(this);
		
		painter = new DefaultHighlighter.DefaultHighlightPainter(DEFAULT_COLOR);
	}
	@Override
	public void clear() {
		sync.clear();
		srcFile = null;
		sync=null;
		codeEditor=null;
		if (tracedEntity != null)
			tracedEntity.removeStackListener(this);
	}
	@Override
	public void entityTraceChanged(Entity e, StackTraceElement[] trace) {
		if (highlight != null)
			codeEditor.getHighlighter().removeHighlight(highlight);
		highlight = null;
		for (StackTraceElement elm:trace) {
			if (elm.getFileName().equals(srcFile.getName()) || elm.getFileName().equals(srcFile.getName()+".java from JavaFileObjectImpl")) {
				// FIXME System.out.println("Please go to line "+elm.getLineNumber());
			}
		}		
	}
	@Override
	public void tracedEntityChanged(Entity e) {
		if (tracedEntity != null)
			tracedEntity.removeStackListener(this);

		tracedEntity = e;
		if (tracedEntity != null)
			tracedEntity.addStackListener(this);
	}
}
