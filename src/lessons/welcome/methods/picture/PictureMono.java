package lessons.welcome.methods.picture;

import java.awt.Color;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.Buggle;
import plm.universe.bugglequest.BuggleWorld;

public class PictureMono extends ExerciseTemplated {

	public PictureMono(Lesson lesson) {
		super(lesson);
		BuggleWorld myWorld =  new BuggleWorld("World",5,5);
		new Buggle(myWorld, "Picasso", 0, 4, Direction.EAST, Color.black, Color.lightGray);
		
		setup(myWorld);
	}
}
