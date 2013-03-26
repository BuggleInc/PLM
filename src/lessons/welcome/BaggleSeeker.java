package lessons.welcome;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;
import jlm.universe.bugglequest.exception.AlreadyHaveBaggleException;

public class BaggleSeeker extends ExerciseTemplated {

	public BaggleSeeker(Lesson lesson) {
		super(lesson);

		BuggleWorld myWorld = new BuggleWorld("Kitchen",7,7);
		for (int i=0;i<7;i++) {
			new Buggle(myWorld, "Cooker "+(i+1), i, 6, Direction.NORTH, Color.black, Color.lightGray);

			try {
				myWorld.newBaggle(i, 6-i);
			} catch (AlreadyHaveBaggleException e) {
				e.printStackTrace();
			}
		}
		setup(myWorld);
	}
}