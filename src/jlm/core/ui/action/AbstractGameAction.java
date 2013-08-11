package jlm.core.ui.action;

import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import jlm.core.model.Game;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;


public abstract class AbstractGameAction extends AbstractAction {

	private static final long serialVersionUID = -1190103028775831188L;
	private String descEnabled, descDisabled;

	protected Game game;
	protected Locale locale;
	protected I18n i18n;

	public AbstractGameAction(Game game, String text) {
		super(text);
		this.game = game;
	}
	
	public AbstractGameAction(Game game, String text, ImageIcon icon) {
		super(text, icon);
		this.game = game;
		this.i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",game.getLocale(), I18nFactory.FALLBACK);

	}
	
	protected void setDescription(String descEnabled, String descDisabled) {
		putValue(SHORT_DESCRIPTION, descEnabled);
		this.descEnabled = descEnabled;
		this.descDisabled = descDisabled;		
	}

	public AbstractGameAction(Game game, String text, ImageIcon icon, Integer mnemonic) {
		this (game,text,icon);
		putValue(MNEMONIC_KEY, mnemonic);
	}

	public Game getGame() {
		return this.game;
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (enabled)
			putValue(SHORT_DESCRIPTION, descEnabled);
		else
			putValue(SHORT_DESCRIPTION, descDisabled);
			
		super.setEnabled(enabled);
	}

}
