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
	private World world;

	public EntityComboListAdapter(Game game) {
		this.game = game;
		this.game.addGameListener(this);
		this.world = this.game.getSelectedWorld();
	}

	@Override
	public Object getElementAt(int index) {
		return this.world.getEntity(index);
	}

	@Override
	public int getSize() {
		return this.world.getEntityCount();
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
			this.world.setSelectedEntity(e);
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
		this.world = this.game.getSelectedWorld();
		fireContentsChanged(this, 0, this.world.getEntityCount() - 1);
	}

	@Override
	public void selectedEntityHasChanged() {
		fireContentsChanged(this, 0, this.world.getEntityCount() - 1);
	}
	
	@Override
	public void selectedWorldWasUpdated() {
		fireContentsChanged(this, 0, this.world.getEntityCount() - 1);
	}

}
