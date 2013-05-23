package jlm.core.ui.action;

import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.ImageIcon;

import jlm.core.HumanLangChangesListener;
import jlm.core.model.Game;

/** Action launched when the Start button gets hit */
public class StartExecution extends AbstractGameAction implements HumanLangChangesListener {

	private static final long serialVersionUID = -4326617501298324713L;
	
	public StartExecution(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
		game.addHumanLangListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (game.getState().equals(Game.GameState.EXECUTION_STARTED) && game.stepModeEnabled()) {
			game.disableStepMode();
			game.allowOneStep();
		} else
			this.game.startExerciseExecution();		
	}

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		setDescription(i18n.tr("Launch the execution of your code"),
				i18n.tr("Cannot launch the execution right now. Wait a bit, or interrupt current activity with the stop button"));		
	}
}
