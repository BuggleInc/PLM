package jlm.core;

/*
 * GameStateListener that implements this interface (in place of GameStateListener) will be garbaged
 * after the notification process if they haven been marked 'dirty'.
 */
public interface DiscardableGameStateListener extends GameStateListener {
	
	public boolean isDirty();
	
}
