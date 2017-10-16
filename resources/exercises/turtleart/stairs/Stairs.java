package turtleart.stairs;

import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Stairs extends ExerciseTemplated {

	public Stairs(FileUtils fileUtils) {

		super("Stairs", "Stairs");

		World myWorld = new TurtleWorld(fileUtils, "Stairs", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 50, 250);
		t.setHeading(-90);
		setup(myWorld);
	}
}
