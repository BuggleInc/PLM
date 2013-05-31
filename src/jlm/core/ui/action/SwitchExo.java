package jlm.core.ui.action;

import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.ImageIcon;

import jlm.core.HumanLangChangesListener;
import jlm.core.model.Game;
import jlm.core.ui.ChooseLectureDialog;


public class SwitchExo extends AbstractGameAction implements HumanLangChangesListener {

	private static final long serialVersionUID = 5113775865916404566L;

	public SwitchExo(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
		game.addHumanLangListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new ChooseLectureDialog();
	}

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		setDescription(i18n.tr("Switch to another exercise"),"");
	}

}
