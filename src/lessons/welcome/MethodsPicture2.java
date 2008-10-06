package lessons.welcome;

import java.awt.Color;

import lessons.ExerciseTemplated;
import lessons.Lesson;
import bugglequest.core.Buggle;
import bugglequest.core.Direction;
import bugglequest.core.World;

public class MethodsPicture2 extends ExerciseTemplated {

	public MethodsPicture2(Lesson lesson) {
		super(lesson);
		name = "Dessiner avec méthode (2)";
		World myWorld =  new World("World",15,15);
		new Buggle(myWorld, "Picasso", 0, 14, Direction.EAST, Color.black, Color.lightGray);

		UIDelay = 20;

		setup(myWorld);
	}
}
