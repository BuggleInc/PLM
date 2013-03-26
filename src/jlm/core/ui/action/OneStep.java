package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import jlm.core.model.Game;


public class OneStep extends AbstractGameAction {

	private static final long serialVersionUID = 930451111824072175L;

	public OneStep(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.game.allowOneStep();		
	}
}
