package jlm.core.ui.action;

import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.ImageIcon;

import jlm.core.HumanLangChangesListener;
import jlm.core.model.Game;


public class Reset extends AbstractGameAction implements HumanLangChangesListener {

	private static final long serialVersionUID = 5113775865916404566L;

	public Reset(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
		game.addHumanLangListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.game.reset();
	}
	

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		setDescription(i18n.tr("Reset your world to the initial state"),i18n.tr("World cannot be reset right now"));		
	}

}
