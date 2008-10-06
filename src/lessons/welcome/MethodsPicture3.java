package lessons.welcome;

import java.awt.Color;

import jlm.bugglequest.Buggle;
import jlm.bugglequest.Direction;
import jlm.bugglequest.World;

import lessons.ExerciseTemplated;
import lessons.Lesson;

public class MethodsPicture3 extends ExerciseTemplated {

	public MethodsPicture3(Lesson lesson) {
		super(lesson);
		name = "Dessiner avec méthode (3)";
		World myWorld =  new World("World",45,45);
		new Buggle(myWorld, "Picasso", 0, 44, Direction.EAST, Color.black, Color.lightGray);
		
		UIDelay = 5;
		
		setup(myWorld);
	}
}
