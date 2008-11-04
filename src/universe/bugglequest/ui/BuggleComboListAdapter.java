package universe.bugglequest.ui;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import universe.World;
import universe.bugglequest.AbstractBuggle;

import jlm.core.Game;
import jlm.core.Logger;
import jlm.event.GameListener;


public class BuggleComboListAdapter extends AbstractListModel implements ComboBoxModel, GameListener {

	private static final long serialVersionUID = -4602618861291726344L;
	private Game game;
	private World world;

	public BuggleComboListAdapter(Game game) {
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
		return this.world.entityCount();
	}

	@Override
	public Object getSelectedItem() {
		return this.game.getSelectedEntity();
	}

	@Override
	public void setSelectedItem(Object anItem) {
		if (anItem instanceof AbstractBuggle) {
			AbstractBuggle b = (AbstractBuggle) anItem;
			this.game.setSelectedEntity(b);
		} else {
			Logger.log("buggleComboListAdapter:setSelectedItem", "parameter is not a buggle");
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
		fireContentsChanged(this, 0, this.world.entityCount() - 1);
	}

	@Override
	public void selectedEntityHasChanged() {
		fireContentsChanged(this, 0, this.world.entityCount() - 1);
	}
	
	@Override
	public void selectedWorldWasUpdated() {
		fireContentsChanged(this, 0, this.world.entityCount() - 1);
	}

}
