package jlm.core.ui;

import javax.swing.JFrame;

import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lecture;
import jlm.core.model.lesson.Exercise.WorldKind;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;


public class AboutWorldDialog extends AbstractAboutDialog {

	private static final long serialVersionUID = 1766486738385426108L;

	public I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	public AboutWorldDialog(JFrame parent) {
		super(parent);
		currentExerciseHasChanged(Game.getInstance().getCurrentLesson().getCurrentExercise());
	}

	@Override
	public void currentExerciseHasChanged(Lecture lecture) {
		if (lecture instanceof Exercise) {
			Exercise exo = (Exercise) lecture;
			setTitle(i18n.tr("About world - ")
					+ exo.getWorldList(WorldKind.CURRENT).get(0).getClass()
					.getSimpleName());
			area.setText(exo.getWorldList(WorldKind.CURRENT).get(0).getAbout());
			area.setCaretPosition(0);
		} else {
			// FIXME: should disable the entry menu when seing a lecture, and close any preexisting window when switching to a lecture
			setVisible(false);
		}
	}
}
