package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;

public class MethodsPicture extends ExerciseTemplated {

	public MethodsPicture(Lesson lesson) {
		super(lesson);
		BuggleWorld myWorld =  new BuggleWorld("World",5,5);
		new Buggle(myWorld, "Picasso", 0, 4, Direction.EAST, Color.black, Color.lightGray);
		
		setup(myWorld);
	}
}
