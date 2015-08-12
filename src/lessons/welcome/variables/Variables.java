package lessons.welcome.variables;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;
import plm.universe.bugglequest.exception.AlreadyHaveBaggleException;

public class Variables extends ExerciseTemplated {

	public Variables(Game game, Lesson lesson) {
		super(game, lesson);
		setToolbox();

		BuggleWorld myWorld = new BuggleWorld(game, "Kitchen",7,7);
		for (int i=0;i<7;i++) {
			new SimpleBuggle(myWorld, "Cooker "+(i+1), i, 6, Direction.NORTH, Color.black, Color.lightGray);

			try {
				myWorld.addBaggle(i, 6-i);
			} catch (AlreadyHaveBaggleException e) {
				e.printStackTrace();
			}
		}
		setup(myWorld);
	}
}