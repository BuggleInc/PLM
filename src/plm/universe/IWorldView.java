package plm.universe;

public interface IWorldView {

	/**
	 * Called every time something changes: entity move, new entity, entity gets destroyed, etc.
	 */
	public void worldHasMoved();
	
	/**
	 * Called when entities are created or destroyed, not when they move
	 */
	public void worldHasChanged(); 

}