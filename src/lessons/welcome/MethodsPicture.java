package lessons.welcome;

import java.awt.Color;

import jlm.bugglequest.Buggle;
import jlm.bugglequest.Direction;
import jlm.bugglequest.World;

import lessons.ExerciseTemplated;
import lessons.Lesson;

public class MethodsPicture extends ExerciseTemplated {

	public MethodsPicture(Lesson lesson) {
		super(lesson);
		name = "Dessiner avec méthode";
		World myWorld =  new World("World",5,5);
		new Buggle(myWorld, "Picasso", 0, 4, Direction.EAST, Color.black, Color.lightGray);
		
		setup(myWorld);
	}
}
