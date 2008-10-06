package bugglequest.core;

public interface IWorldView {

	/**
	 * Called every time something changes: buggle move, new buggle, buggle gets destroyed, etc.
	 */
	public void worldHasMoved();
	
	/**
	 * Called when buggles are created or destroyed, not when they move
	 */
	public void worldHasChanged(); 

}