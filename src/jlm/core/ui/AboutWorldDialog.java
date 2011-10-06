package jlm.core.ui;

import javax.swing.JFrame;

import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lecture;

public class AboutWorldDialog extends AbstractAboutDialog {

	private static final long serialVersionUID = 1766486738385426108L;

	public AboutWorldDialog(JFrame parent) {
		super(parent);
		currentExerciseHasChanged(Game.getInstance().getCurrentLesson().getCurrentExercise());
	}

	@Override
	public void currentExerciseHasChanged(Lecture lecture) {
		if (lecture instanceof Exercise) {
			Exercise exo = (Exercise) lecture;
			setTitle("About world - "
					+ exo.getCurrentWorld().get(0).getClass()
					.getSimpleName());
			area.setText(exo.getCurrentWorld().get(0).getAbout());
			area.setCaretPosition(0);
		} else {
			// FIXME: should disable the entry menu when seing a lecture, and close any preexisting window when switching to a lecture
			setVisible(false);
		}
	}
}
