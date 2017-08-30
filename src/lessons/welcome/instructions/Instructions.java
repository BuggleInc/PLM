package lessons.welcome.instructions;

import java.awt.Color;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class Instructions extends ExerciseTemplated {

	public Instructions(Lesson lesson) {
		super(lesson);
		tabName="Code";

		BuggleWorld myWorld = new BuggleWorld("Training World", 7,7);
		new SimpleBuggle(myWorld, "Rookie", 2, 4, Direction.NORTH, Color.black, Color.lightGray);
		
		setup(myWorld);
	}
}
