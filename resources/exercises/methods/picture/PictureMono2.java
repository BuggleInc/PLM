package methods.picture;

import java.awt.Color;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class PictureMono2 extends ExerciseTemplated {

	public PictureMono2(FileUtils fileUtils) {
		super("PictureMono2");
		//setToolbox();
		BuggleWorld myWorld =  new BuggleWorld(fileUtils, "World",21,21);
		new SimpleBuggle(myWorld, "Picasso", 0, 20, Direction.EAST, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
