package lessons.welcome.methods.basics;

import java.awt.Color;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.Buggle;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.exception.AlreadyHaveBaggleException;

public class Methods extends ExerciseTemplated {

	public Methods(Lesson lesson) {
		super(lesson);

		BuggleWorld myWorld =  new BuggleWorld("Donut World",7,7);
		new Buggle(myWorld, "Homer", 0, 6, Direction.NORTH, Color.black, Color.lightGray);

		try {
			for (int i=0;i<7;i++) 
				myWorld.addBaggle(i,i);
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}
		
		setup(myWorld);
	}
}
