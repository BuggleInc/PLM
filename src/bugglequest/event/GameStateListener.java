package bugglequest.event;

import bugglequest.core.GameState;

public interface GameStateListener {

	public void stateChanged(GameState type) ;
	
}
