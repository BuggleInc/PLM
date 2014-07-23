package plm.core.ui;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import plm.core.GameListener;
import plm.core.model.Game;
import plm.core.model.Logger;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.universe.World;

public class WorldComboListAdapter extends AbstractListModel<World> implements ComboBoxModel<World>, GameListener {

	private static final long serialVersionUID = -4669130955472219209L;
	private Game game;
	private World selectedWorld;
	private Exercise currentExercise;
	
	public WorldComboListAdapter(Game game) {
		this.game = game;
		this.game.addGameListener(this);
		if (game.getCurrentLesson() != null && game.getCurrentLesson().getCurrentExercise() instanceof Exercise) {
			this.currentExercise = (Exercise) this.game.getCurrentLesson().getCurrentExercise();
			this.selectedWorld = this.game.getSelectedWorld();
		}
	}

	@Override
	public World getElementAt(int index) {
		return currentExercise == null?null: this.currentExercise.getWorld(index);
	}

	@Override
	public int getSize() {
		return currentExercise == null?0: currentExercise.getWorldCount();
	}

	@Override
	public Object getSelectedItem() {
		return currentExercise == null?null: selectedWorld;
	}

	@Override
	public void setSelectedItem(Object anItem) {
		if (anItem instanceof World) {
			World w = (World) anItem;
			this.selectedWorld = w;
			this.game.setSelectedWorld(w);
		} else {
			Logger.log("WordComboListAdapter:setSelectedItem", "parameter is not a world");
		}
	}

	
	@Override
	public void currentExerciseHasChanged(Lecture lect) {
		if (lect instanceof Exercise) {
			this.currentExercise = (Exercise) lect;
			this.selectedWorld = game.getSelectedWorld();
			fireContentsChanged(this, 0, this.currentExercise.getWorldCount()-1);
		} else {
			currentExercise = null;
			selectedWorld = null;
		}
	}
	
	@Override
	public void selectedWorldHasChanged(World w) {
		this.selectedWorld = w;
		fireContentsChanged(this, 0, this.currentExercise.getWorldCount()-1);		
	}
	
	@Override
	public void selectedEntityHasChanged() { /* don't care */ }
	
	@Override
	public void selectedWorldWasUpdated() { /* don't care */ }
}
