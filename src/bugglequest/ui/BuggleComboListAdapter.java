package bugglequest.ui;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import bugglequest.core.AbstractBuggle;
import bugglequest.core.Game;
import bugglequest.core.Logger;
import bugglequest.core.World;
import bugglequest.event.GameListener;

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
		return this.world.getBuggle(index);
	}

	@Override
	public int getSize() {
		return this.world.bugglesCount();
	}

	@Override
	public Object getSelectedItem() {
		return this.game.getSelectedBuggle();
	}

	@Override
	public void setSelectedItem(Object anItem) {
		if (anItem instanceof AbstractBuggle) {
			AbstractBuggle b = (AbstractBuggle) anItem;
			this.game.setSelectedBuggle(b);
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
		fireContentsChanged(this, 0, this.world.bugglesCount() - 1);
	}

	@Override
	public void selectedBuggleHasChanged() {
		fireContentsChanged(this, 0, this.world.bugglesCount() - 1);
	}
	
	@Override
	public void selectedWorldWasUpdated() {
		fireContentsChanged(this, 0, this.world.bugglesCount() - 1);
	}

}
