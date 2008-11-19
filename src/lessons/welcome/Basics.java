package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;

import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;

public class Basics extends ExerciseTemplated {

	public Basics(Lesson lesson) {
		super(lesson);
		name = "Rudiments du Java";
		tabName="Program";

		BuggleWorld myWorld = new BuggleWorld("Training World", 7,7);
		new Buggle(myWorld, "Rookie", 3, 3, Direction.NORTH, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
