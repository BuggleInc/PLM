package turtleart.circletwo;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class CircleTwo extends ExerciseTemplated {
	
	public CircleTwo() {
		
		super("CircleTwo", "CircleTwo");

		World myWorld = new TurtleWorld("CircleTwo", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 35, 149);
		t.setHeading(-90);
		setup(myWorld);
	}
}
