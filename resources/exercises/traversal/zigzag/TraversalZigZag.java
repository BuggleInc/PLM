package traversal.zigzag.traversalzigzag;

import java.awt.Color;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class TraversalZigZag extends ExerciseTemplated {

	public TraversalZigZag(FileUtils fileUtils) {
		super("TraversalZigZag", "TraversalZigZag");
		tabName = "ZigZag";
		//setToolbox();

		BuggleWorld myWorld = new BuggleWorld(fileUtils, "Grid",7,7);
		for (int i=0; i<7;i++) {
			myWorld.putTopWall(i, 0);
			myWorld.putLeftWall(0, i);
		}
		
		new SimpleBuggle(myWorld, "Walker", 0, 0, Direction.NORTH, Color.black, Color.red);

		setup(myWorld);
	}
}
