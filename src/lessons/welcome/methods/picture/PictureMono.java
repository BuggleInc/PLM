package lessons.welcome.methods.picture;

import java.awt.Color;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class PictureMono extends ExerciseTemplated {

	public PictureMono(Lesson lesson) {
		super(lesson);
		BuggleWorld myWorld =  new BuggleWorld("World",7,7);
		new SimpleBuggle(myWorld, "Picasso", 0, 6, Direction.EAST, Color.black, Color.lightGray);
		
		setup(myWorld);
	}
}
