package turtleart.triangleflat;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class TriangleFlat extends ExerciseTemplated {

	public TriangleFlat() {

		super("TriangleFlat", "TriangleFlat");

		World myWorld = new TurtleWorld("TriangleFlat", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 50, 250);
		t.setHeading(-90);
		setup(myWorld);
	}
}
