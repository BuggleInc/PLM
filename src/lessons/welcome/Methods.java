package lessons.welcome;

import java.awt.Color;

import lessons.ExerciseTemplated;
import lessons.Lesson;
import bugglequest.core.Buggle;
import bugglequest.core.Direction;
import bugglequest.core.World;
import bugglequest.exception.AlreadyHaveBaggleException;

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
