package turtleart.smallsquare;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class SmallSquare extends ExerciseTemplated {

	public SmallSquare() {

		super("SmallSquare", "SmallSquare");

		World myWorld = new TurtleWorld("SmallSquare", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 50, 150);
		t.setHeading(-90);
		setup(myWorld);
	}
}
