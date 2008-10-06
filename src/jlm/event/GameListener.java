package jlm.event;

public interface GameListener {

	// when a lesson becomes current lesson of the game
	public void currentLessonHasChanged() ;
	
	// when a lesson is added or removed from the game
	public void lessonsChanged() ;

	// when an exercise becomes current exercise of the game
	public void currentExerciseHasChanged() ;
	
	// when a world is selected as the current world
	public void selectedWorldHasChanged(); 
	
	// when a buggle is selected as the current buggle in a world
	public void selectedBuggleHasChanged();

	// when buggles are replaced in the current 
	public void selectedWorldWasUpdated();
}