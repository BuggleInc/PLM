package lessons.welcome;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class MethodsArgs extends ExerciseTemplated {

	public MethodsArgs(Lesson lesson) {
		super(lesson);
		tabName = "Program";

		BuggleWorld myWorld = new BuggleWorld("Buggles Party",7,7);
		new Buggle(myWorld, "Homer", 0, 6, Direction.NORTH, Color.black, Color.lightGray);
		new Buggle(myWorld, "Bart", 1, 2, Direction.SOUTH, Color.black, Color.lightGray);
		new Buggle(myWorld, "Lisa", 2, 4, Direction.SOUTH, Color.black, Color.lightGray);
		new Buggle(myWorld, "Maggie", 3, 1, Direction.SOUTH, Color.black, Color.lightGray);
		new Buggle(myWorld, "Marge", 4, 0, Direction.SOUTH, Color.black, Color.lightGray);
		new Buggle(myWorld, "Itchy", 5, 3, Direction.NORTH, Color.black, Color.lightGray);
		new Buggle(myWorld, "Scratchy", 6, 5, Direction.NORTH, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
