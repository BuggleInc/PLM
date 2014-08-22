package lessons.welcome.methods.picture;

import java.awt.Color;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.Buggle;
import plm.universe.bugglequest.BuggleWorld;

public class PatternPicture extends ExerciseTemplated {

	public PatternPicture(Lesson lesson) {
		super(lesson);
		BuggleWorld myWorld =  new BuggleWorld("World",8,8);
		
		for (int i=0;i<8;i++) {
			myWorld.putTopWall (i, 0);
			myWorld.putLeftWall(0, i);
		}
		
		new Buggle(myWorld, "Picasso", 0, 7, Direction.EAST, Color.black, Color.lightGray);
		
		setup(myWorld);
	}
}
