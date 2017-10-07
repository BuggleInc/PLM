package turtleart.disktwo;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class DiskTwo extends ExerciseTemplated {
	
	public DiskTwo() {
		
		super("DiskTwo", "DiskTwo");

		World myWorld = new TurtleWorld("DiskTwo", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 150, 150);
		t.setHeading(-90);
		setup(myWorld);
	}
}
