package lessons.recursion.logo.spiral;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class SpiralUse extends ExerciseTemplated {

	public SpiralUse(Game game, Lesson lesson) {
		super(game, lesson);

		/* Create initial situation */
		TurtleWorld myWorld = new TurtleWorld(game, "Sheet", 400, 400);
		new Turtle(myWorld, "Hawksbill", 200, 200);

		setup(myWorld);
	}
}
