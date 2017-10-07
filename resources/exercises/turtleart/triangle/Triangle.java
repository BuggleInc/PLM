package turtleart.triangle;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Triangle extends ExerciseTemplated {

	public Triangle() {

		super("Triangle", "Triangle");

		World myWorld = new TurtleWorld("Triangle", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 50, 250);
		t.setHeading(-90);
		setup(myWorld);
	}
}
