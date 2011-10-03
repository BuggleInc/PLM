package jlm.core;

public interface GameListener {

	// when a lesson becomes current lesson of the game
	public void currentLessonHasChanged() ;
	
	// when an exercise becomes current exercise of the game
	public void currentExerciseHasChanged() ;
	
	// when a world is selected as the current world
	public void selectedWorldHasChanged(); 
	
	// when an entity is selected as the current entity in a world
	public void selectedEntityHasChanged();

	// when entities are replaced in the current 
	public void selectedWorldWasUpdated();
}