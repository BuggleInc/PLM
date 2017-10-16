package recursion.logo.square;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class FourSquare extends ExerciseTemplated {

	public FourSquare(FileUtils fileUtils) {
		super("FourSquare", "FourSquare");

		/* Create initial situation */
		World myWorld = new TurtleWorld(fileUtils, "WhiteBoard", 400, 400);

		new Turtle(myWorld, "Hawksbill", 200, 200);
		setup(myWorld);
	}
}
