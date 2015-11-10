package lessons.recursion.logo.square;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class FourSquare extends ExerciseTemplated {

	public FourSquare(Game game, Lesson lesson) {
		super(game, lesson);

		/* Create initial situation */
		World myWorld = new TurtleWorld(game, "WhiteBoard", 400, 400);

		new Turtle(myWorld, "Hawksbill", 200, 200);
		setup(myWorld);
	}
}
