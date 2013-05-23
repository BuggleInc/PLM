package jlm.core;

import jlm.core.model.Game;

public interface GameStateListener {

	public void stateChanged(Game.GameState type) ;
	
}
