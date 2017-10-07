package methods.picture;

import java.awt.Color;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class PictureMono1 extends ExerciseTemplated {

	public PictureMono1() {
		super("PictureMono1", "PictureMono1");
		//setToolbox();
		BuggleWorld myWorld =  new BuggleWorld("World",7,7);
		new SimpleBuggle(myWorld, "Picasso", 0, 6, Direction.EAST, Color.black, Color.lightGray);
		
		setup(myWorld);
	}
}
