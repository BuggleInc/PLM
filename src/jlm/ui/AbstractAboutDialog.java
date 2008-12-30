package jlm.ui;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import jlm.core.Game;
import jlm.event.GameListener;
import net.miginfocom.swing.MigLayout;

public abstract class AbstractAboutDialog extends JDialog implements GameListener {

	private static final long serialVersionUID = -6550658679688214378L;
	protected JEditorPane area = new JEditorPane("text/html","");
	
	protected AbstractAboutDialog(JFrame parent) {
		super(parent, "", false);
		Game.getInstance().addGameListener(this);
		
		setResizable(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		
		setMinimumSize(new Dimension(600,400));
		
		area.setEditable(false);
		
		setLayout(new MigLayout("fill"));
		add(new JScrollPane(area),"grow");
	}
		
	public void currentExerciseHasChanged() {}
	public void currentLessonHasChanged() {}
	public void lessonsChanged() {}
	public void selectedEntityHasChanged() {}
	public void selectedWorldHasChanged() {}
	public void selectedWorldWasUpdated() {}
	
}
