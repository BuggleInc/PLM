package lessons.welcome.methods.picture;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class MethodsPicture extends ExerciseTemplated {

	public MethodsPicture(Lesson lesson) {
		super(lesson);
		BuggleWorld myWorld =  new BuggleWorld("World",15,15);
		myWorld.setDelay(20);
		new Buggle(myWorld, "Picasso", 0, 14, Direction.EAST, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
