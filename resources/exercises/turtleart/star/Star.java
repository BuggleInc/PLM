package turtleart.star;

import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Star extends ExerciseTemplated {

	public Star(FileUtils fileUtils) {

		super("Star");

		World myWorld = new TurtleWorld(fileUtils, "Star", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 150, 200);
		t.setHeading(-90);
		setup(myWorld);
	}
}
