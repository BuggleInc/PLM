package turtleart.housetree;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class HouseTree extends ExerciseTemplated {
	
	public HouseTree() {
		
		super("HouseTree", "HouseTree");

		World myWorld = new TurtleWorld("HouseTree", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 50, 150);
		t.setHeading(-90);
		setup(myWorld);
	}
}
