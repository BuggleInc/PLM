package jlm.ui;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import jlm.core.Game;
import jlm.core.Logger;
import jlm.event.GameListener;

import lessons.Lesson;

public class LessonComboListAdapter extends AbstractListModel implements ComboBoxModel, GameListener {

	private static final long serialVersionUID = 2283697155928402290L;
	private Game game;

	public LessonComboListAdapter(Game game) {
		this.game = game;
		this.game.addGameListener(this);
	}

	@Override
	public Object getElementAt(int index) {
		return this.game.getLessons().get(index);
	}

	@Override
	public int getSize() {
		return this.game.getLessons().size();
	}

	@Override
	public Object getSelectedItem() {
		return this.game.getCurrentLesson();
	}

	@Override
	public void setSelectedItem(Object anItem) {
		if (anItem instanceof Lesson) {
			this.game.setCurrentLesson((Lesson) anItem);
		} else {
			Logger.log("LessonComboBoxAdapter:setSelectedItem", "parameter is not a lesson");
		}
	}
	
	@Override
	public void currentLessonHasChanged() {
		fireContentsChanged(this, 0, this.game.getCurrentLesson().exerciseCount()-1);
	}

	@Override
	public void lessonsChanged() {
		fireContentsChanged(this, 0, this.game.getCurrentLesson().exerciseCount()-1);
	}

	@Override
	public void currentExerciseHasChanged() {
		// don't care
	}
	
	@Override
	public void selectedWorldHasChanged() {
		// don't care
	}
	
	@Override
	public void selectedEntityHasChanged() {
		// don't care
	}
	
	@Override
	public void selectedWorldWasUpdated() {
		// don't care
	}
}
