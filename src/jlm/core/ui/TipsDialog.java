package jlm.core.ui;

import javax.swing.JFrame;

import jlm.core.model.Game;
import jlm.core.model.lesson.Lecture;

@SuppressWarnings("serial")
public class TipsDialog extends AbstractAboutDialog {

	public TipsDialog(JFrame parent) {
		super(parent);
		currentExerciseHasChanged(Game.getInstance().getCurrentLesson().getCurrentExercise());
	}

	@Override
	public void currentExerciseHasChanged(Lecture lect) {
		setTitle("Tips");
		this.area.setText("no tips");
		this.area.setCaretPosition(0);
	}
	
	public void setText(String txt) {
		this.area.setText(txt);
		this.area.setCaretPosition(0);
	}

}
