package methods.picture;

import java.awt.Color;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsPicture extends ExerciseTemplated {

	public MethodsPicture() {
		super("MethodsPicture", "MethodsPicture");
		//setToolbox();
		BuggleWorld myWorld =  new BuggleWorld("World",15,15);
		new SimpleBuggle(myWorld, "Picasso", 0, 14, Direction.EAST, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
