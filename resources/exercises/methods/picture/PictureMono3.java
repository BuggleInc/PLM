package methods.picture;

import java.awt.Color;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class PictureMono3 extends ExerciseTemplated {

	public PictureMono3() {
		super("PictureMono3", "PictureMono3");
		//setToolbox();
		BuggleWorld myWorld =  new BuggleWorld("World",63,63);
		new SimpleBuggle(myWorld, "Picasso", 0, 62, Direction.EAST, Color.black, Color.lightGray);
				
		setup(myWorld);
	}
}
