package lessons.welcome;

import java.awt.Color;

import jlm.bugglequest.Buggle;
import jlm.bugglequest.Direction;
import jlm.bugglequest.World;

import lessons.ExerciseTemplated;
import lessons.Lesson;

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
