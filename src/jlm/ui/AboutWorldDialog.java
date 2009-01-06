package jlm.ui;

import javax.swing.JFrame;

import jlm.core.Game;

public class AboutWorldDialog extends AbstractAboutDialog {

	private static final long serialVersionUID = 1766486738385426108L;

	public AboutWorldDialog(JFrame parent) {
		super(parent);
		currentExerciseHasChanged();
	}

	@Override
	public void currentExerciseHasChanged() {
		setTitle("About world - "
				+ Game.getInstance().getCurrentLesson().getCurrentExercise().getCurrentWorld().get(0).getClass()
						.getSimpleName());
		area.setText(Game.getInstance().getCurrentLesson().getCurrentExercise().getCurrentWorld().get(0).getAbout());
		area.setCaretPosition(0);
	}

}
