package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import jlm.core.model.Game;
import jlm.core.model.GameState;


public class StartExecution extends AbstractGameAction {

	private static final long serialVersionUID = -4326617501298324713L;
	
	public StartExecution(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (game.getState().equals(GameState.EXECUTION_STARTED) && game.stepModeEnabled()) {
			game.disableStepMode();
			game.allowOneStep();
		} else
			this.game.startExerciseExecution();		
	}
}
