package lessons.welcome.methods.returning;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;
import plm.universe.bugglequest.exception.AlreadyHaveBaggleException;

public class MethodsReturning extends ExerciseTemplated {

	public MethodsReturning(Game game, Lesson lesson) {
		super(game, lesson);
		tabName = "Program";
		setToolbox();

		BuggleWorld[] myWorld = new BuggleWorld[3];
		for (int i=0; i<3;i++) {
			myWorld[i] = new BuggleWorld(game, "World "+(i+1),7,7);
			new SimpleBuggle(myWorld[i], "Searcher", 0, 6, Direction.NORTH, Color.black, Color.lightGray);
		}

		try {
			myWorld[0].addBaggle(3, 2);
			myWorld[1].addBaggle(5, 1);
			myWorld[2].addBaggle(2, 6);
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}

		setup(myWorld);
	}
}
