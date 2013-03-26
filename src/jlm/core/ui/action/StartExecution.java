package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import jlm.core.model.Game;
import jlm.core.model.GameState;

/** Action launched when the Start button gets hit */
public class StartExecution extends AbstractGameAction {

	private static final long serialVersionUID = -4326617501298324713L;
	static final String descEnabled = "Launch the execution of your code"; 
	static final String descDisabled = "Cannot launch the execution right now. Wait a bit, or interrupt current activity with the stop button";
	
	public StartExecution(Game game, String text, ImageIcon icon) {
		super(game, text, icon, descEnabled, descDisabled);
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
