package turtleart.circleying;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class CircleYing extends ExerciseTemplated {
	
	public CircleYing() {
		
		super("CircleYing", "CircleYing");

		World myWorld = new TurtleWorld("CircleYing", 300, 300);
		Turtle t = new Turtle(myWorld, "Hawksbill", 35, 149);
		t.setHeading(-90);
		setup(myWorld);
	}
}
