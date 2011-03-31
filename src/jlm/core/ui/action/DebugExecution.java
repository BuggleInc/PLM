package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import jlm.core.model.Game;
import jlm.core.model.GameState;


public class DebugExecution extends AbstractGameAction {

	private static final long serialVersionUID = 930451111824072175L;

	public DebugExecution(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (game.getState().equals(GameState.EXECUTION_STARTED))
			game.allowOneStep();
		else			
			game.startExerciseStepExecution();		
	}
}
