package turtleart.housetree;

import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class HouseTree extends ExerciseTemplated {
	
	public HouseTree(FileUtils fileUtils) {
		
		super("HouseTree");

		World myWorld = new TurtleWorld(fileUtils, "HouseTree", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 50, 150);
		t.setHeading(-90);
		setup(myWorld);
	}
}
