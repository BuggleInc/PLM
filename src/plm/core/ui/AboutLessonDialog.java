package plm.core.ui;

import javax.swing.JFrame;

import plm.core.model.Game;
import plm.core.model.lesson.Lecture;


public class AboutLessonDialog extends AbstractAboutDialog {

	private static final long serialVersionUID = 1766486738385426108L;
	
	public AboutLessonDialog(JFrame parent) {
		super(parent);
		currentExerciseHasChanged(null);
	}

	@Override
	public void currentExerciseHasChanged(Lecture lect) {
		setTitle(Game.i18n.tr("About lesson - ") + Game.getInstance().getCurrentLesson().getName());

		area.setText(Game.getInstance().getCurrentLesson().getAbout());
		area.setCaretPosition(0);
	}

}
