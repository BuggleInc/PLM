package lessons.welcome;

import java.awt.Color;

import jlm.bugglequest.Buggle;
import jlm.bugglequest.Direction;
import jlm.bugglequest.World;

import lessons.ExerciseTemplated;
import lessons.Lesson;

public class BasicsDrawG extends ExerciseTemplated {

	public BasicsDrawG(Lesson lesson) {
		super(lesson);
		name = "Rudiments du Java (3)";
		tabName = "Program";

		World myWorld = new World("Training World", 7, 7);
		new Buggle(myWorld, "Rookie", 5, 1, Direction.NORTH, Color.black, Color.blue);

		setup(myWorld);
	}
}
