package jlm.ui;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import jlm.core.Game;
import jlm.event.GameListener;
import net.miginfocom.swing.MigLayout;

public class AboutWorldDialog extends JDialog implements GameListener {

	private static final long serialVersionUID = -1800747039420103759L;
	private static AboutWorldDialog instance = null;
	JEditorPane area = new JEditorPane("text/html","");
	
	public static AboutWorldDialog getInstance() {
		if (AboutWorldDialog.instance == null)
			AboutWorldDialog.instance = new AboutWorldDialog();
		return AboutWorldDialog.instance;
	}
	
	private AboutWorldDialog() {
		super(MainFrame.getInstance(), "About this world", false);
		Game.getInstance().addGameListener(this);
		
		setResizable(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		
		setMinimumSize(new Dimension(600,400));

		area.setEditable(false);
		
		setLayout(new MigLayout("fill"));
		add(new JScrollPane(area),"grow");
		
		currentExerciseHasChanged();
	}
	
	
	public void currentExerciseHasChanged() {
		setTitle("About world "+Game.getInstance().getCurrentLesson().getCurrentExercise().getCurrentWorld().get(0).getClass().getSimpleName());
		area.setText(Game.getInstance().getCurrentLesson().getCurrentExercise().getCurrentWorld().get(0).getAbout());
		area.setCaretPosition(0);
	}

	public void currentLessonHasChanged() {} // who cares?
	public void lessonsChanged() {} // so what?
	public void selectedEntityHasChanged() {} // tell your mum, not me
	public void selectedWorldHasChanged() {} // leave me alone
	public void selectedWorldWasUpdated() {} // get away
	
}
