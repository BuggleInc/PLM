package jlm.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import jlm.core.Game;


public class EnableDisableStepMode extends AbstractGameAction {

	private static final long serialVersionUID = 6069620616939212312L;

	public EnableDisableStepMode(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.game.stepModeEnabled()) {
			this.game.disableStepMode();
		} else {
			this.game.enableStepMode();
		}
	}
}
