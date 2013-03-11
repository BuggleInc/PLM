package jlm.core.ui;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;


import jlm.core.GameListener;
import jlm.core.model.Game;
import jlm.core.model.lesson.Lecture;
import jlm.universe.World;

public abstract class AbstractAboutDialog extends JFrame implements GameListener {

	private static final long serialVersionUID = -6550658679688214378L;
	MarkdownDocument md_doc;
	MarkdownEditorView editeur;

	protected AbstractAboutDialog(JFrame parent) {
		super();
		Game.getInstance().addGameListener(this);

		setResizable(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());

		setMinimumSize(new Dimension(600,400));

		md_doc = new MarkdownDocument();
		editeur = new MarkdownEditorView(md_doc);
		if(Global.admin)
			add(editeur);
		else
			add(new JScrollPane(editeur.apercu));
	}
	
	public void maj(){
		this.removeAll();
		if(Global.admin)
			add(editeur);
		else
			add(new JScrollPane(editeur.apercu));
		this.repaint();
		this.validate();
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
