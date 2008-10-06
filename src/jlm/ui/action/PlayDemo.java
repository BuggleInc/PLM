package jlm.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import jlm.bugglequest.Game;


public class PlayDemo extends AbstractGameAction {

	private static final long serialVersionUID = 5113775865916404566L;

	public PlayDemo(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.game.startExerciseDemoExecution();		
	}
}
