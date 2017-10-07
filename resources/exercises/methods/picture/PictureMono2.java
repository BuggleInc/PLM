package methods.picture;

import java.awt.Color;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class PictureMono2 extends ExerciseTemplated {

	public PictureMono2() {
		super("PictureMono2", "PictureMono2");
		//setToolbox();
		BuggleWorld myWorld =  new BuggleWorld("World",21,21);
		new SimpleBuggle(myWorld, "Picasso", 0, 20, Direction.EAST, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
