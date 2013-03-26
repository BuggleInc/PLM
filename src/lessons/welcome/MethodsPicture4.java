package lessons.welcome;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class MethodsPicture4 extends ExerciseTemplated {

	public MethodsPicture4(Lesson lesson) {
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
