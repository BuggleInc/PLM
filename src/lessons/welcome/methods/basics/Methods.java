package lessons.welcome.methods.basics;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;
import plm.universe.bugglequest.exception.AlreadyHaveBaggleException;

public class Methods extends ExerciseTemplated {

	public Methods(Game game, Lesson lesson) {
		super(game, lesson);
		setToolbox();

		BuggleWorld myWorld =  new BuggleWorld(game, "Donut World",7,7);
		new SimpleBuggle(myWorld, "Homer", 0, 6, Direction.NORTH, Color.black, Color.lightGray);

		try {
			for (int i=0;i<7;i++) 
				myWorld.addBaggle(i,i);
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}
		
		setup(myWorld);
	}
}
