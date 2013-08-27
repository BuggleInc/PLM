package plm.core.ui.action;

import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.ImageIcon;

import plm.core.HumanLangChangesListener;
import plm.core.model.Game;


public class QuitGame extends AbstractGameAction implements HumanLangChangesListener {

	private static final long serialVersionUID = 5778501209753480269L;

	public QuitGame(Game game, String text, ImageIcon icon, Integer mnemonic) {
		super(game, text, icon, mnemonic);
		game.addHumanLangListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		game.quit();
	}

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		setDescription(i18n.tr("Quit the application"),i18n.tr("Impossible to quit the application right now"));		
	}

}
