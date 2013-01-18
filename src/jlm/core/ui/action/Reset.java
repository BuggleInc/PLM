package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import jlm.core.model.Game;


public class Reset extends AbstractGameAction {

	private static final long serialVersionUID = 5113775865916404566L;

	public Reset(Game game, String text, ImageIcon icon) {
		super(game, text, icon,
				"Reset your world to the initial state",
				"World cannot be reset right now");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.game.reset();
	}
}
