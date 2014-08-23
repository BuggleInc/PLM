package plm.core;

import plm.core.model.Game;

public interface GameStateListener {

	public void stateChanged(Game.GameState type) ;
	
}
