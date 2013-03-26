package jlm.core.ui.action;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import jlm.core.model.Game;


public abstract class AbstractGameAction extends AbstractAction {

	private static final long serialVersionUID = -1190103028775831188L;
	private String descEnabled, descDisabled;

	protected Game game;

	public AbstractGameAction(Game game, String text) {
		super(text);
		this.game = game;
	}
	
	public AbstractGameAction(Game game, String text, ImageIcon icon) {
		super(text, icon);
		this.game = game;
	}
	
	public AbstractGameAction(Game game, String text, ImageIcon icon, String descEnabled, String descDisabled) {
		this(game,text,icon);
		putValue(SHORT_DESCRIPTION, descEnabled);
		this.descEnabled = descEnabled;
		this.descDisabled = descDisabled;
	}

	public AbstractGameAction(Game game, String text, ImageIcon icon, String descEnabled,String descDisabled, Integer mnemonic) {
		this (game,text,icon, descEnabled,descDisabled);
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
