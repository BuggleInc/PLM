package methods.picture;

import java.awt.Color;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class PatternPicture extends ExerciseTemplated {

	public PatternPicture(FileUtils fileUtils) {
		super("PatternPicture");
		//setToolbox();
		BuggleWorld myWorld =  new BuggleWorld(fileUtils, "World",8,8);
		
		for (int i=0;i<8;i++) {
			myWorld.putTopWall (i, 0);
			myWorld.putLeftWall(0, i);
		}
		
		new SimpleBuggle(myWorld, "Picasso", 0, 7, Direction.EAST, Color.black, Color.lightGray);
		
		setup(myWorld);
	}
}
