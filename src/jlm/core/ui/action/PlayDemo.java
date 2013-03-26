package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import jlm.core.model.Game;


public class PlayDemo extends AbstractGameAction {

	private static final long serialVersionUID = 5113775865916404566L;

	public PlayDemo(Game game, String text, ImageIcon icon) {
		super(game, text, icon,
				"Run the demo of what you should code for this exercise",
				"Impossible to run the demo right now");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.game.startExerciseDemoExecution();		
	}
}
