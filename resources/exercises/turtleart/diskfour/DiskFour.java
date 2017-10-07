package turtleart.diskfour;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class DiskFour extends ExerciseTemplated {
	
	public DiskFour() {
		
		super("DiskFour", "DiskFour");

		World myWorld = new TurtleWorld("DiskFour", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 150, 150);
		t.setHeading(-90);
		setup(myWorld);
	}
}
