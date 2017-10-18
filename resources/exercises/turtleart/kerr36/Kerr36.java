package turtleart.kerr36;

import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Kerr36 extends ExerciseTemplated {
	
	public Kerr36(FileUtils fileUtils) {
		
		super("Kerr36");

		World myWorld = new TurtleWorld(fileUtils, "Kerr36", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 150, 150);
		t.setHeading(-90);
		setup(myWorld);
	}
}
