package plm.core.ui.action;

import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.ImageIcon;

import plm.core.HumanLangChangesListener;
import plm.core.model.Game;
import plm.core.ui.ChooseLessonDialog;
import plm.core.ui.MainFrame;

public class SwitchLesson extends AbstractGameAction implements
		HumanLangChangesListener {

	private static final long serialVersionUID = 2553345612252156139L;

	public SwitchLesson(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
		game.addHumanLangListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new ChooseLessonDialog();
		MainFrame.getInstance().setVisible(false);
	}

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		// TODO Auto-generated method stub

	}

}
