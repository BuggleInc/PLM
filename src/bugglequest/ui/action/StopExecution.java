package bugglequest.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import bugglequest.core.Game;

public class StopExecution extends AbstractGameAction {

	private static final long serialVersionUID = -4563140493957678216L;
	
	public StopExecution(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.game.stopExerciseExecution();		
	}
}
