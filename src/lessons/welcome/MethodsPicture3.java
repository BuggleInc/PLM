package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.universe.Direction;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;

public class MethodsPicture3 extends ExerciseTemplated {

	public MethodsPicture3(Lesson lesson) {
		super(lesson);
		BuggleWorld myWorld =  new BuggleWorld("World",45,45);
		myWorld.setDelay(5);
		new Buggle(myWorld, "Picasso", 0, 44, Direction.EAST, Color.black, Color.lightGray);
				
		setup(myWorld);
	}
}
