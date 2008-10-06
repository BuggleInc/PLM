package lessons.welcome;

import java.awt.Color;

import lessons.ExerciseTemplated;
import lessons.Lesson;
import bugglequest.core.Buggle;
import bugglequest.core.Direction;
import bugglequest.core.World;

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
