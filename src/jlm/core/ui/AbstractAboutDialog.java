package jlm.core.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import com.petebevin.markdown.MarkdownProcessor;


import jlm.core.GameListener;
import jlm.core.model.Game;
import jlm.core.model.lesson.Lecture;
import jlm.universe.World;

public abstract class AbstractAboutDialog extends JFrame implements GameListener {

	private static final long serialVersionUID = -6550658679688214378L;
	MarkdownDocument md_doc;
	MarkdownEditorView editor;
	JPanel main_pane;
	MarkdownProcessor markdownProcessor;

	protected AbstractAboutDialog(JFrame parent) {
		super();
		Game.getInstance().addGameListener(this);

		setResizable(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());

		setSize(new Dimension(600,400));
		main_pane = new JPanel(new BorderLayout());
		add(main_pane);

		markdownProcessor = new MarkdownProcessor();
		md_doc = new MarkdownDocument();
		editor = new MarkdownEditorView(md_doc);
		JScrollPane jsp = null;
		if(Global.admin)
			jsp = new JScrollPane(editor);
		else
			jsp = new JScrollPane(editor.view);
		
		main_pane.add(jsp);

	    JScrollBar verticalScrollBar = jsp.getVerticalScrollBar();
	    JScrollBar horizontalScrollBar = jsp.getHorizontalScrollBar();
	    verticalScrollBar.setValue(verticalScrollBar.getMinimum());
	    horizontalScrollBar.setValue(horizontalScrollBar.getMinimum());
	}
	
	public void maj(){
		this.main_pane.removeAll();
		String path = md_doc.getPath();
		md_doc = new MarkdownDocument(path);
		editor = new MarkdownEditorView(md_doc);
		JScrollPane jsp = null;
		if(Global.admin)
			jsp = new JScrollPane(editor);
		else
			jsp = new JScrollPane(editor.view);
		
		main_pane.add(jsp);

	    JScrollBar verticalScrollBar = jsp.getVerticalScrollBar();
	    JScrollBar horizontalScrollBar = jsp.getHorizontalScrollBar();
	    verticalScrollBar.setValue(verticalScrollBar.getMinimum());
	    horizontalScrollBar.setValue(horizontalScrollBar.getMinimum());
	    
		this.main_pane.repaint();
		this.main_pane.validate();
	}

	@Override
	public void currentExerciseHasChanged(Lecture l)   { /* I dont care I'm a punk */ }
	@Override
	public void currentLessonHasChanged()              { /* I dont care I'm a punk */ }
	@Override
	public void selectedEntityHasChanged()             { /* I dont care I'm a punk */ }
	@Override
	public void selectedWorldHasChanged(World w)       { /* I dont care I'm a punk */ }
	@Override
	public void selectedWorldWasUpdated()              { /* I dont care I'm a punk */ }
}
