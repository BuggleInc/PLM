package methods.args;

import java.awt.Color;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsArgs extends ExerciseTemplated {

	public MethodsArgs() {
		super("MethodsArgs", "MethodsArgs");
		tabName = "Program";
		//setToolbox();

		BuggleWorld myWorld = new BuggleWorld("Buggles Party",7,7);
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
