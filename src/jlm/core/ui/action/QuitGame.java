package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import jlm.core.model.Game;


public class QuitGame extends AbstractGameAction {

	private static final long serialVersionUID = 5778501209753480269L;

	public QuitGame(Game game, String text, ImageIcon icon, Integer mnemonic) {
		super(game, text, icon, "Quit the application","Impossible to quit the application right now", mnemonic);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		game.quit();
	}

}
