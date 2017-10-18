package turtleart.house;

import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class House extends ExerciseTemplated {
	
	public House(FileUtils fileUtils) {
		
		super("House");

		World myWorld = new TurtleWorld(fileUtils, "House", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 50, 250);
		t.setHeading(-90);
		setup(myWorld);
	}
}
