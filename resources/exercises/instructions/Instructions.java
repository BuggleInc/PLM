package instructions;

import java.awt.Color;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class Instructions extends ExerciseTemplated {

	public Instructions(FileUtils fileUtils) {
		super("Instructions");
		tabName="Program";

		BuggleWorld myWorld = new BuggleWorld(fileUtils, "Training World", 7,7);
		new SimpleBuggle(myWorld, "Rookie", 2, 4, Direction.NORTH, Color.black, Color.lightGray);
		
		setup(myWorld);
	}
}
