package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;

import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;

public class MethodsPicture2 extends ExerciseTemplated {

	public MethodsPicture2(Lesson lesson) {
		super(lesson);
		BuggleWorld myWorld =  new BuggleWorld("World",15,15);
		myWorld.setDelay(20);
		new Buggle(myWorld, "Picasso", 0, 14, Direction.EAST, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
