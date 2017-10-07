package methods.picture;

import java.awt.Color;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsPictureLarge extends ExerciseTemplated {

	public MethodsPictureLarge() {
		super("MethodsPictureLarge", "MethodsPictureLarge");
		//setToolbox();
		BuggleWorld myWorld =  new BuggleWorld("World",45,45);
		new SimpleBuggle(myWorld, "Picasso", 0, 44, Direction.EAST, Color.black, Color.lightGray);
				
		setup(myWorld);
	}
}
