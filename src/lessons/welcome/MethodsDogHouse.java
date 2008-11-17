package lessons.welcome;

import java.awt.Color;

import lessons.ExerciseTemplated;
import lessons.Lesson;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;

public class MethodsDogHouse extends ExerciseTemplated {

	public MethodsDogHouse(Lesson lesson) {
		super(lesson);
		name = "Construire avec méthode";
		BuggleWorld myWorld =  new BuggleWorld("World",7,7);
		new Buggle(myWorld, "Puppy", 0, 6, Direction.EAST, Color.red, Color.red);
		
		setup(myWorld);
	}
}
