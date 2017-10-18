package recursion.logo.spiral;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class SpiralUse extends ExerciseTemplated {

	public SpiralUse(FileUtils fileUtils) {
		super("SpiralUse");

		/* Create initial situation */
		TurtleWorld myWorld = new TurtleWorld(fileUtils, "Sheet", 400, 400);
		new Turtle(myWorld, "Hawksbill", 200, 200);

		setup(myWorld);
	}
}
