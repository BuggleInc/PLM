package turtleart.circlesquare;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class CircleSquare extends ExerciseTemplated {

	public CircleSquare() {

		super("CircleSquare", "CircleSquare");

		World myWorld = new TurtleWorld("CircleSquare", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 50, 200);
		t.setHeading(-90);
		setup(myWorld);
	}
}
