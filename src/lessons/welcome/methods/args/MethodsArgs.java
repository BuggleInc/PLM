package lessons.welcome.methods.args;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsArgs extends ExerciseTemplated {

	public MethodsArgs(Game game, Lesson lesson) {
		super(game, lesson);
		tabName = "Program";
		setToolbox();

		BuggleWorld myWorld = new BuggleWorld(game, "Buggles Party",7,7);
		new SimpleBuggle(myWorld, "Homer", 0, 6, Direction.NORTH, Color.black, Color.lightGray);
		new SimpleBuggle(myWorld, "Bart", 1, 2, Direction.SOUTH, Color.black, Color.lightGray);
		new SimpleBuggle(myWorld, "Lisa", 2, 4, Direction.SOUTH, Color.black, Color.lightGray);
		new SimpleBuggle(myWorld, "Maggie", 3, 1, Direction.SOUTH, Color.black, Color.lightGray);
		new SimpleBuggle(myWorld, "Marge", 4, 0, Direction.SOUTH, Color.black, Color.lightGray);
		new SimpleBuggle(myWorld, "Itchy", 5, 3, Direction.NORTH, Color.black, Color.lightGray);
		new SimpleBuggle(myWorld, "Scratchy", 6, 5, Direction.NORTH, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
