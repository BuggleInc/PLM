package turtleart.polygon6;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Polygon6 extends ExerciseTemplated {

	public Polygon6() {

		super("Polygon6", "Polygon6");

		World myWorld = new TurtleWorld("Polygon6", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 81, 190);
		t.setHeading(-90);
		setup(myWorld);
	}
}
