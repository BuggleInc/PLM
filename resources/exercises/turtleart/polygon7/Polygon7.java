package turtleart.polygon7;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Polygon7 extends ExerciseTemplated {

	public Polygon7() {

		super("Polygon7", "Polygon7");

		World myWorld = new TurtleWorld("Polygon7", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 65,190);
		t.setHeading(-90);
		setup(myWorld);
	}
}
