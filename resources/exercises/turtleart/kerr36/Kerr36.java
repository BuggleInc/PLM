package turtleart.kerr36;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Kerr36 extends ExerciseTemplated {
	
	public Kerr36() {
		
		super("Kerr36", "Kerr36");

		World myWorld = new TurtleWorld("Kerr36", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 150, 150);
		t.setHeading(-90);
		setup(myWorld);
	}
}
