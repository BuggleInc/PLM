package lessons.welcome;

import java.awt.Color;

import jlm.bugglequest.Buggle;
import jlm.bugglequest.Direction;
import jlm.bugglequest.World;

import lessons.ExerciseTemplated;
import lessons.Lesson;

public class Basics extends ExerciseTemplated {

	public Basics(Lesson lesson) {
		super(lesson);
		name = "Rudiments du Java";
		tabName="Program";

		World myWorld = new World("Training World", 7,7);
		new Buggle(myWorld, "Rookie", 3, 3, Direction.NORTH, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
