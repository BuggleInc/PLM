package jlm.core.ui;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.model.session.SourceFile;
import jlm.universe.Entity;
import jlm.universe.IEntityStackListener;

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

		/*
		InputMap imap = codeEditor.getInputMap();
		ActionMap amap = codeEditor.getActionMap();		
		for (KeyStroke ks:imap.allKeys()) 
			System.out.println("key: "+ks+" -> "+imap.get(ks)+" -> "+amap.get(imap.get(ks)));
		 */
		
		codeEditor.setCaretPosition(0);
		//((SyntaxDocument) codeEditor.getDocument()).setCurrentEditedLineNumber(0); TODO: find the new way of doing so in jsyntaxpane 0.9.5
		
		/* Create a synchronization element, and connect it to the editor */
		sync = new SourceFileDocumentSynchronizer(codeEditor.getEditorKit());
		sync.setDocument(codeEditor.getDocument());
		codeEditor.getDocument().addDocumentListener(sync);
		
		/* Connect the synchronization element to the source file */
		srcFile.setListener(sync);
		sync.setSourceFile(srcFile);
		
		codeEditor.setText(srcFile.getBody());
		((jsyntaxpane.SyntaxDocument) codeEditor.getDocument()).clearUndos();
		
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
		/* TODO: The symbol setCurrentEditedLineNumber() is not in jsyntaxpane anymore. 
		 * But the following code was not working anyway... 
		for (StackTraceElement elm:trace) {
			// added defenses against NPE because sometimes (launched from a .jar file, a NullPointerException might happen...)
			if (elm.getFileName() != null && codeEditor != null && (elm.getFileName().equals(srcFile.getName()) || elm.getFileName().equals(srcFile.getName()+".java from JavaFileObjectImpl"))) {				
				SyntaxDocument sd = ((SyntaxDocument) codeEditor.getDocument());
				if (sd != null)
					sd.setCurrentEditedLineNumber(elm.getLineNumber());
				codeEditor.repaint();
			}
		}		
		 */
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
