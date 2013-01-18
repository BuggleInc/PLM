package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import jlm.core.model.Game;


public class StopExecution extends AbstractGameAction {

	private static final long serialVersionUID = -4563140493957678216L;
	
	public StopExecution(Game game, String text, ImageIcon icon) {
		super(game, text, icon, "Stop your code", "No execution to stop right now");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.game.stopExerciseExecution();		
	}
}
