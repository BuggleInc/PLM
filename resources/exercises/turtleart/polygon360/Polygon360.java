package turtleart.polygon360;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Polygon360 extends ExerciseTemplated {

	public Polygon360() {

		super("Polygon360", "Polygon360");

		World myWorld = new TurtleWorld("Polygon360", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 35, 149);
		t.setHeading(-90);
		setup(myWorld);
	}
}
