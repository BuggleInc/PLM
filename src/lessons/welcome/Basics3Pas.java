package lessons.welcome;

import java.awt.Color;

import lessons.ExerciseTemplated;
import lessons.Lesson;
import bugglequest.core.Buggle;
import bugglequest.core.Direction;
import bugglequest.core.World;

public class Basics3Pas extends ExerciseTemplated {

	public Basics3Pas(Lesson lesson) {
		super(lesson);
		name = "Rudiments du Java (2)";
		tabName="Program";
		
		World myWorld = new World("Training World", 7,7);
		new Buggle(myWorld, "Rookie", 3, 3, Direction.NORTH, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
