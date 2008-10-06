package bugglequest.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import bugglequest.core.Game;

public class QuitGame extends AbstractGameAction {

	private static final long serialVersionUID = 5778501209753480269L;

	public QuitGame(Game game, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(game, text, icon, desc, mnemonic);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		game.quit();
		System.exit(0);		
	}

}
