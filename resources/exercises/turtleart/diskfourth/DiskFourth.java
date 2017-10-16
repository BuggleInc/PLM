package turtleart.diskfourth;

import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class DiskFourth extends ExerciseTemplated {
	
	public DiskFourth(FileUtils fileUtils) {
		
		super("DiskFourth", "DiskFourth");

		World myWorld = new TurtleWorld(fileUtils, "DiskFourth", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 150, 150);
		t.setHeading(-90);
		setup(myWorld);
	}
}
