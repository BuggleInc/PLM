package lessons.welcome;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;
import jlm.universe.bugglequest.exception.AlreadyHaveBaggleException;

public class Methods extends ExerciseTemplated {

	public Methods(Lesson lesson) {
		super(lesson);

		BuggleWorld myWorld =  new BuggleWorld("Donut World",7,7);
		new Buggle(myWorld, "Homer", 0, 6, Direction.NORTH, Color.black, Color.lightGray);

		try {
			for (int i=0;i<7;i++) 
				myWorld.newBaggle(i,i);
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}
		
		setup(myWorld);
	}
}
