package plm.core.ui;

import javax.swing.JFrame;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.model.Game;


public class AboutLessonDialog extends AbstractAboutDialog {

	private static final long serialVersionUID = 1766486738385426108L;
	
	public I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	public AboutLessonDialog(JFrame parent) {
		super(parent);
		currentLessonHasChanged();
	}

	@Override
	public void currentLessonHasChanged() {
		setTitle(i18n.tr("About lesson - ") + Game.getInstance().getCurrentLesson().getName());

		area.setText(Game.getInstance().getCurrentLesson().getAbout());
		area.setCaretPosition(0);
	}

}
