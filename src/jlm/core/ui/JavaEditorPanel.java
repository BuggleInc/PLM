package jlm.core.ui;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.model.lesson.SourceFile;
import jlm.universe.Entity;
import jlm.universe.IEntityStackListener;
import jsyntaxpane.SyntaxDocument;

public class JavaEditorPanel extends JScrollPane implements IEditorPanel,IEntityStackListener {
	private static final long serialVersionUID = 1L;
	SourceFile srcFile;
	SourceFileDocumentSynchronizer sync ;
	JEditorPane codeEditor;
	Entity tracedEntity;

	public JavaEditorPanel(SourceFile srcFile, ProgrammingLanguage lang) {
		super();
		this.srcFile = srcFile;

		codeEditor = new JEditorPane();
		setViewportView(codeEditor);
		codeEditor.setContentType("text/"+lang.getLang().toLowerCase());
		codeEditor.setCaretPosition(0);
		((SyntaxDocument) codeEditor.getDocument()).setCurrentEditedLineNumber(0);
		
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
		
		for (StackTraceElement elm:trace) {
			// TODO: added defenses against NPE because sometimes (launched from a .jar file, a NullPointerException might happen...)
			if (elm.getFileName() != null && codeEditor != null && (elm.getFileName().equals(srcFile.getName()) || elm.getFileName().equals(srcFile.getName()+".java from JavaFileObjectImpl"))) {				
				SyntaxDocument sd = ((SyntaxDocument) codeEditor.getDocument());
				if (sd != null)
					sd.setCurrentEditedLineNumber(elm.getLineNumber());
				codeEditor.repaint();
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
