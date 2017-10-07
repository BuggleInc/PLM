package turtleart.housemany;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class HouseMany extends ExerciseTemplated {
	
	public HouseMany() {
		
		super("HouseMany", "HouseMany");

		World myWorld = new TurtleWorld("HouseMany", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 50, 250);
		t.setHeading(-90);
		setup(myWorld);
	}
}
