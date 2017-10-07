package turtleart.kerr40;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Kerr40 extends ExerciseTemplated {
	
	public Kerr40() {
		
		super("Kerr40", "Kerr40");

		World myWorld = new TurtleWorld("Kerr40", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 150, 150);
		t.setHeading(-90);
		setup(myWorld);
	}
}
