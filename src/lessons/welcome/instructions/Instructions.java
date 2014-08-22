package lessons.welcome.instructions;

import java.awt.Color;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.Buggle;
import plm.universe.bugglequest.BuggleWorld;

public class Instructions extends ExerciseTemplated {

	public Instructions(Lesson lesson) {
		super(lesson);
		tabName="Program";

		BuggleWorld myWorld = new BuggleWorld("Training World", 7,7);
		new Buggle(myWorld, "Rookie", 2, 4, Direction.NORTH, Color.black, Color.lightGray);
		
		setup(myWorld);
	}
}
