package jlm.universe.bugglequest.mapeditor;

import jlm.universe.Entity;
import jlm.universe.World;

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
	 * Event fired when the currently selected entity changes
	 * @param ent new entity under radar
	 */
	void setSelectedEntity(Entity ent);
}
