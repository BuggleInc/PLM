package turtleart.circleten;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class CircleTen extends ExerciseTemplated {
	
	public CircleTen() {
		
		super("CircleTen", "CircleTen");

		World myWorld = new TurtleWorld("CircleTen", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 150, 150);
		t.setHeading(-90);
		setup(myWorld);
	}
}
