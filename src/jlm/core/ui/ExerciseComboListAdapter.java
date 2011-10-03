package jlm.core.ui;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import jlm.core.GameListener;
import jlm.core.model.Game;
import jlm.core.model.Logger;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lesson;

public class ExerciseComboListAdapter extends AbstractListModel implements ComboBoxModel, GameListener {

	private static final long serialVersionUID = -7437100074443148108L;
	private Game game;
	private Lesson lesson; // only used to read, writes are performed on game instance 
	
	public ExerciseComboListAdapter(Game game) {
		this.game = game;
		this.game.addGameListener(this);
		this.lesson = game.getCurrentLesson();
	}

	@Override
	public Object getElementAt(int index) {
		return this.lesson.getExercise(index);
	}

	@Override
	public int getSize() {
		return this.lesson.exerciseCount();
	}

	@Override
	public Object getSelectedItem() {
		return this.lesson.getCurrentExercise();
	}

	@Override
	public void setSelectedItem(Object anItem) {
		if (anItem instanceof Exercise) {
			Exercise exo = (Exercise) anItem;
			//if (exo.getLesson().isAccessible(exo)) {
				//this.lesson.setCurrentExercise(exo);
				this.game.setCurrentExercise(exo);
			//}			
		} else {
			Logger.log("ExerciseComboBoxAdapter:setSelectedItem", "parameter is not an exercise");
		}
	}

	@Override
	public void currentLessonHasChanged() {
		this.lesson = game.getCurrentLesson();
		//fireContentsChanged(this, 0, this.lesson.exerciseCount()-1);
	}

	@Override
	public void lessonsChanged() { /* don't care */ }

	@Override
	public void currentExerciseHasChanged() {
		fireContentsChanged(this, 0, this.lesson.exerciseCount()-1);		
	}

	@Override
	public void selectedWorldHasChanged() { /* don't care */ }
	
	@Override
	public void selectedEntityHasChanged() { /* don't care */ }
	
	@Override
	public void selectedWorldWasUpdated() { /* don't care */ }
}
