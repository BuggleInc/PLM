package turtleart.diskfour;

import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class DiskFour extends ExerciseTemplated {
	
	public DiskFour(FileUtils fileUtils) {
		
		super("DiskFour");

		World myWorld = new TurtleWorld(fileUtils, "DiskFour", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 150, 150);
		t.setHeading(-90);
		setup(myWorld);
	}
}
