package lessons.welcome;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class MethodsDogHouse extends ExerciseTemplated {

	public MethodsDogHouse(Lesson lesson) {
		super(lesson);
		BuggleWorld myWorld =  new BuggleWorld("World",7,7);
		new Buggle(myWorld, "Puppy", 0, 6, Direction.EAST, Color.red, Color.red);
		
		setup(myWorld);
	}
}
