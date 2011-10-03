package jlm.core.ui;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import jlm.core.GameListener;
import jlm.core.model.Game;
import jlm.core.model.Logger;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lecture;
import jlm.universe.World;

public class WorldComboListAdapter extends AbstractListModel implements ComboBoxModel, GameListener {

	private static final long serialVersionUID = -4669130955472219209L;
	private Game game;
	private World selectedWorld;
	private Exercise currentExercise;
	
	public WorldComboListAdapter(Game game) {
		this.game = game;
		this.game.addGameListener(this);
		if (game.getCurrentLesson().getCurrentExercise() instanceof Exercise) {
			this.currentExercise = (Exercise) this.game.getCurrentLesson().getCurrentExercise();
			this.selectedWorld = this.game.getSelectedWorld();
		}
	}

	@Override
	public Object getElementAt(int index) {
		return currentExercise == null?null: this.currentExercise.getWorld(index);
	}

	@Override
	public int getSize() {
		return currentExercise == null?0: currentExercise.worldCount();
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
			fireContentsChanged(this, 0, this.currentExercise.worldCount()-1);
		} else {
			currentExercise = null;
			selectedWorld = null;
		}
	}
	
	@Override
	public void currentLessonHasChanged() { /* don't care */ }

	@Override
	public void selectedWorldHasChanged(World w) {
		this.selectedWorld = w;
		fireContentsChanged(this, 0, this.currentExercise.worldCount()-1);		
	}
	
	@Override
	public void selectedEntityHasChanged() { /* don't care */ }
	
	@Override
	public void selectedWorldWasUpdated() { /* don't care */ }
}
