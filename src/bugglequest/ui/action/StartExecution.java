package bugglequest.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import bugglequest.core.Game;

public class StartExecution extends AbstractGameAction {

	private static final long serialVersionUID = -4326617501298324713L;
	
	public StartExecution(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.game.startExerciseExecution();		
	}
}
