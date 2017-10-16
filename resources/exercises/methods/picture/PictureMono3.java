package methods.picture;

import java.awt.Color;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class PictureMono3 extends ExerciseTemplated {

	public PictureMono3(FileUtils fileUtils) {
		super("PictureMono3", "PictureMono3");
		//setToolbox();
		BuggleWorld myWorld =  new BuggleWorld(fileUtils, "World",63,63);
		new SimpleBuggle(myWorld, "Picasso", 0, 62, Direction.EAST, Color.black, Color.lightGray);
				
		setup(myWorld);
	}
}
