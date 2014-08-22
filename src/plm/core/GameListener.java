package plm.core;

import plm.core.model.lesson.Lecture;
import plm.universe.World;

public interface GameListener {

	// when an exercise becomes current exercise of the game
	public void currentExerciseHasChanged(Lecture lect) ;
	
	// when a world is selected as the current world
	public void selectedWorldHasChanged(World newWorld); 
	
	// when an entity is selected as the current entity in a world
	public void selectedEntityHasChanged();

	// when entities are replaced in the current 
	public void selectedWorldWasUpdated();
}