package turtleart.smallsquare;

import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class SmallSquare extends ExerciseTemplated {

	public SmallSquare(FileUtils fileUtils) {

		super("SmallSquare", "SmallSquare");

		World myWorld = new TurtleWorld(fileUtils, "SmallSquare", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 50, 150);
		t.setHeading(-90);
		setup(myWorld);
	}
}
