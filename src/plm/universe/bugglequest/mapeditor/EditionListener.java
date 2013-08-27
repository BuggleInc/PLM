package plm.universe.bugglequest.mapeditor;

import plm.universe.Entity;
import plm.universe.World;

public interface EditionListener {
	/** 
	 * Inform the listener that the edited world just changed 
	 * @param w the new world to edit
	 */
	void setWorld(World w);
	/** 
	 * Event fired each time that something changes in the edited world. That's quite often, actually
	 */
	void worldEdited();
	/**
	 * Event fired when the currently selection changes
	 * @param x x-coordinate of selection
	 * @param y y-coordinate of selection
	 * @param ent new entity under radar. May be null if there is no entity in selected cell
	 */
	void selectedChanged(int x, int y, Entity ent);
}
