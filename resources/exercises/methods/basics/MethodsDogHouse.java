package methods.basics;

import java.awt.Color;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsDogHouse extends ExerciseTemplated {

	public MethodsDogHouse() {
		super("MethodsDogHouse", "MethodsDogHouse");
		BuggleWorld myWorld =  new BuggleWorld("World",7,7);
		new SimpleBuggle(myWorld, "Puppy", 0, 6, Direction.EAST, Color.red, Color.red);
		
		setup(myWorld);
	}
}
