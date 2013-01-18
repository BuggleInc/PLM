package lessons.welcome;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class Basics extends ExerciseTemplated {

	public Basics(Lesson lesson) {
		super(lesson);
		tabName="Program";

		BuggleWorld myWorld = new BuggleWorld("Training World", 7,7);
		new Buggle(myWorld, "Rookie", 2, 4, Direction.NORTH, Color.black, Color.lightGray);
		
		setup(myWorld);
	}
}
