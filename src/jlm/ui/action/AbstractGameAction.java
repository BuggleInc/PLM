package jlm.ui.action;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import jlm.bugglequest.Game;


public abstract class AbstractGameAction extends AbstractAction {

	private static final long serialVersionUID = -1190103028775831188L;

	protected Game game;

	public AbstractGameAction(Game game, String text) {
		super(text);
		this.game = game;
	}
	
	public AbstractGameAction(Game game, String text, ImageIcon icon) {
		super(text, icon);
		this.game = game;
	}
	
	public AbstractGameAction(Game game, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(text, icon);
		putValue(SHORT_DESCRIPTION, desc);
		putValue(MNEMONIC_KEY, mnemonic);
		this.game = game;
	}

	public Game getGame() {
		return this.game;
	}

}
