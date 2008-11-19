package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;

import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;

public class BasicsDrawG extends ExerciseTemplated {

	public BasicsDrawG(Lesson lesson) {
		super(lesson);
		name = "Rudiments du Java (3)";
		tabName = "Program";

		BuggleWorld myWorld = new BuggleWorld("Training World", 7, 7);
		new Buggle(myWorld, "Rookie", 5, 1, Direction.NORTH, Color.black, Color.blue);

		setup(myWorld);
	}
}
