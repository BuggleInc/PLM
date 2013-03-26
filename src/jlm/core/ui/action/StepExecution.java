package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import jlm.core.model.Game;
import jlm.core.model.GameState;


public class StepExecution extends AbstractGameAction {

	private static final long serialVersionUID = 930451111824072175L;

	public StepExecution(Game game, String text, ImageIcon icon) {
		super(game, text, icon, 
				"Execute one step of your code", 
				"Impossible to step your code now. Need to stop the execution first?");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (game.getState().equals(GameState.EXECUTION_STARTED))
			game.allowOneStep();
		else			
			game.startExerciseStepExecution();		
	}
}
