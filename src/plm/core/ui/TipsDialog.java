package plm.core.ui;

import javax.swing.JFrame;

import plm.core.model.Game;
import plm.core.model.lesson.Lecture;

@SuppressWarnings("serial")
public class TipsDialog extends AbstractAboutDialog {

	public TipsDialog(JFrame parent) {
		super(parent);
		currentExerciseHasChanged(game.getCurrentLesson().getCurrentExercise());
		area.setEditorKit(new PlmHtmlEditorKit(game.getCurrentLesson().getCurrentExercise()));

	}

	@Override
	public void currentExerciseHasChanged(Lecture lect) {
		setTitle(Game.i18n.tr("Tips"));
		this.area.setText(Game.i18n.tr("(no tips to display)"));
		this.area.setCaretPosition(0);
	}
	
	public void setText(String txt) {
		this.area.setText(txt);
		this.area.setCaretPosition(0);
	}

}
