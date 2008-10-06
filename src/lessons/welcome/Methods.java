package lessons.welcome;

import java.awt.Color;

import jlm.bugglequest.Buggle;
import jlm.bugglequest.Direction;
import jlm.bugglequest.World;
import jlm.exception.AlreadyHaveBaggleException;

import lessons.ExerciseTemplated;
import lessons.Lesson;

public class Methods extends ExerciseTemplated {

	public Methods(Lesson lesson) {
		super(lesson);
		name = "Méthodes";
		World myWorld =  new World("Donut World",7,7);
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
