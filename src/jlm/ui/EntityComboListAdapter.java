package jlm.ui;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import jlm.core.Game;
import jlm.core.Logger;
import jlm.event.GameListener;
import jlm.universe.Entity;
import jlm.universe.World;


public class EntityComboListAdapter extends AbstractListModel implements ComboBoxModel, GameListener {

	private static final long serialVersionUID = -4602618861291726344L;
	private Game game;
	private World[] worlds;

	public EntityComboListAdapter(Game game) {
		this.game = game;
		this.game.addGameListener(this);
		this.worlds = this.game.getSelectedWorlds();
	}

	@Override
	public Object getElementAt(int index) {
		return this.worlds[0].getEntity(index);
	}

	@Override
	public int getSize() {
		return this.worlds[0].getEntityCount();
	}

	@Override
	public Object getSelectedItem() {
		return this.game.getSelectedEntity();
	}

	@Override
	public void setSelectedItem(Object anItem) {
		if (anItem instanceof Entity) {
			Entity e = (Entity) anItem;
			this.game.setSelectedEntity(e);
			this.worlds[0].setSelectedEntity(e);
			/* Also inform the objective world that it was changed */
			for (World w:worlds)
				w.notifyWorldUpdatesListeners();
		} else {
			Logger.log("entityComboListAdapter:setSelectedItem", "parameter is not an entity");
		}
	}

	@Override
	public void currentExerciseHasChanged() {
		// don't care
	}

	@Override
	public void currentLessonHasChanged() {
		// don't care
	}

	@Override
	public void lessonsChanged() {
		// don't care
	}

	@Override
	public void selectedWorldHasChanged() {
		this.worlds = this.game.getSelectedWorlds();
		fireContentsChanged(this, 0, this.worlds[0].getEntityCount() - 1);
	}

	@Override
	public void selectedEntityHasChanged() {
		fireContentsChanged(this, 0, this.worlds[0].getEntityCount() - 1);
	}
	
	@Override
	public void selectedWorldWasUpdated() {
		fireContentsChanged(this, 0, this.worlds[0].getEntityCount() - 1);
	}

}
