package turtleart.flower;

import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Flower extends ExerciseTemplated {
	
	public Flower(FileUtils fileUtils) {
		
		super("Flower", "Flower");

		World myWorld = new TurtleWorld(fileUtils, "Flower", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 90, 175);
		t.setHeading(-90);
		setup(myWorld);
	}
}
