package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import jlm.core.model.Game;


public class CleanUpSession extends AbstractGameAction {

	private static final long serialVersionUID = 5778501209753480269L;

	public CleanUpSession(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		game.clearSession();
	}

}
