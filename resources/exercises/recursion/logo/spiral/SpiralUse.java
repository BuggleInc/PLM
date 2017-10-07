package recursion.logo.spiral;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class SpiralUse extends ExerciseTemplated {

	public SpiralUse() {
		super("SpiralUse", "SpiralUse");

		/* Create initial situation */
		TurtleWorld myWorld = new TurtleWorld("Sheet", 400, 400);
		new Turtle(myWorld, "Hawksbill", 200, 200);

		setup(myWorld);
	}
}
