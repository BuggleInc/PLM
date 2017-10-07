package turtleart.polygon20;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Polygon20 extends ExerciseTemplated {

	public Polygon20() {

		super("Polygon20", "Polygon20");

		World myWorld = new TurtleWorld("Polygon20", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 55, 165);
		t.setHeading(-90);
		setup(myWorld);
	}
}
