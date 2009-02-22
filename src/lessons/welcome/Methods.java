package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;
import universe.bugglequest.exception.AlreadyHaveBaggleException;

public class Methods extends ExerciseTemplated {

	public Methods(Lesson lesson) {
		super(lesson);

		BuggleWorld myWorld =  new BuggleWorld("Donut World",7,7);
		new Buggle(myWorld, "Homer", 0, 6, Direction.NORTH, Color.black, Color.lightGray);

		try {
			for (int i=0;i<7;i++) 
				myWorld.getCell(i, i).newBaggle();
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}
		
		setup(myWorld);
	}
}
