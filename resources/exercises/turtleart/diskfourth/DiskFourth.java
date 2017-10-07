package turtleart.diskfourth;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class DiskFourth extends ExerciseTemplated {
	
	public DiskFourth() {
		
		super("DiskFourth", "DiskFourth");

		World myWorld = new TurtleWorld("DiskFourth", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 150, 150);
		t.setHeading(-90);
		setup(myWorld);
	}
}
