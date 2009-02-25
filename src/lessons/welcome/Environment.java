package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;

public class Environment extends ExerciseTemplated {
	public Environment(Lesson lesson) {
		super(lesson);
		tabName = "Source Code";
		
		BuggleWorld myWorld = new BuggleWorld("Training Camp",7,7);
		new Buggle(myWorld, "Noob", 3, 3, Direction.NORTH, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
