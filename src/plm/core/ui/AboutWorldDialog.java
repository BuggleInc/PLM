package plm.core.ui;

import javax.swing.JFrame;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.ProgLangChangesListener;
import plm.core.model.Game;
import plm.core.model.ProgrammingLanguage;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Exercise.WorldKind;


public class AboutWorldDialog extends AbstractAboutDialog implements ProgLangChangesListener {

	private static final long serialVersionUID = 1766486738385426108L;

	public I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	public AboutWorldDialog(JFrame parent) {
		super(parent);
		currentExerciseHasChanged(Game.getInstance().getCurrentLesson().getCurrentExercise());
		Game.getInstance().addProgLangListener(this);
	}

	@Override
	public void currentExerciseHasChanged(Lecture lecture) {
		if (lecture instanceof Exercise) {
			Exercise exo = (Exercise) lecture;
			setTitle(i18n.tr("About world - {0}",
					exo.getWorlds(WorldKind.CURRENT).get(0).getClass().getSimpleName()));
			area.setText(exo.getWorlds(WorldKind.CURRENT).get(0).getAbout());
			area.setCaretPosition(0);
		} else {
			// FIXME: should disable the entry menu when seing a lecture, and close any preexisting window when switching to a lecture
			setVisible(false);
		}
	}

	@Override
	public void currentProgrammingLanguageHasChanged(ProgrammingLanguage newLang) {
		int pos = area.getCaretPosition();
		area.setText(((Exercise) Game.getInstance().getCurrentLesson().getCurrentExercise()).getWorlds(WorldKind.CURRENT).get(0).getAbout());
		area.setCaretPosition(pos);
	}
}
