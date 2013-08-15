package lessons.welcome.methods.picture;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class PictureMono extends ExerciseTemplated {

	public PictureMono(Lesson lesson) {
		super(lesson);
		BuggleWorld myWorld =  new BuggleWorld("World",5,5);
		new Buggle(myWorld, "Picasso", 0, 4, Direction.EAST, Color.black, Color.lightGray);
		
		setup(myWorld);
	}
}
