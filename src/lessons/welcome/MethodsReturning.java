package lessons.welcome;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;
import jlm.universe.bugglequest.exception.AlreadyHaveBaggleException;

public class MethodsReturning extends ExerciseTemplated {

	public MethodsReturning(Lesson lesson) {
		super(lesson);
		tabName = "Program";

		BuggleWorld[] myWorld = new BuggleWorld[3];
		for (int i=0; i<3;i++) {
			myWorld[i] = new BuggleWorld("World "+(i+1),7,7);
			new Buggle(myWorld[i], "Searcher", 0, 6, Direction.NORTH, Color.black, Color.lightGray);
		}

		try {
			myWorld[0].newBaggle(3, 2);
			myWorld[1].newBaggle(5, 1);
			myWorld[2].newBaggle(2, 6);
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}

		setup(myWorld);
	}
}
