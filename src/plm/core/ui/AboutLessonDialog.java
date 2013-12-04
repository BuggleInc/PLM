package plm.core.ui;

import javax.swing.JFrame;

import plm.core.model.Game;


public class AboutLessonDialog extends AbstractAboutDialog {

	private static final long serialVersionUID = 1766486738385426108L;
	
	public AboutLessonDialog(JFrame parent) {
		super(parent);
		currentLessonHasChanged();
	}

	@Override
	public void currentLessonHasChanged() {
		setTitle(Game.i18n.tr("About lesson - ") + Game.getInstance().getCurrentLesson().getName());

		area.setText(Game.getInstance().getCurrentLesson().getAbout());
		area.setCaretPosition(0);
	}

}
