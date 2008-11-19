package jlm.ui;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import jlm.core.Game;
import jlm.event.GameListener;
import net.miginfocom.swing.MigLayout;

public class AboutLessonDialog extends JDialog implements GameListener {

	private static final long serialVersionUID = -1800747039420103759L;
	private static AboutLessonDialog instance = null;
	JEditorPane area = new JEditorPane("text/html","");
	JLabel label = new JLabel();
	
	public static AboutLessonDialog getInstance() {
		if (AboutLessonDialog.instance == null)
			AboutLessonDialog.instance = new AboutLessonDialog();
		return AboutLessonDialog.instance;
	}
	
	private AboutLessonDialog() {
		super(MainFrame.getInstance(), "About lesson", false);
		Game.getInstance().addGameListener(this);
		
		setResizable(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		
		setMinimumSize(new Dimension(300,200));

		area.setEditable(false);
		
		setLayout(new MigLayout("fill"));
		add(label,"wrap");
		add(new JScrollPane(area),"grow");
		
		currentLessonHasChanged();
	}
	
	
	public void currentLessonHasChanged() {
		label.setText("Lesson: "+Game.getInstance().getCurrentLesson().getName());
		area.setText(Game.getInstance().getCurrentLesson().getAbout());
		area.setCaretPosition(0);
	}

	public void currentExerciseHasChanged() {} // who cares?
	public void lessonsChanged() {} // so what?
	public void selectedEntityHasChanged() {} // tell your mum, not me
	public void selectedWorldHasChanged() {} // leave me alone
	public void selectedWorldWasUpdated() {} // get away
	
}
