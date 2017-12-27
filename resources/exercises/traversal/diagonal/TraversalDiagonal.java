package traversal.diagonal;

import java.awt.Color;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class TraversalDiagonal extends ExerciseTemplated {

	public TraversalDiagonal(FileUtils fileUtils) {
		super("TraversalDiagonal");
		tabName = "Diagonal";

		BuggleWorld myWorld = new BuggleWorld(fileUtils, "Grid",7,7);
		for (int i=0; i<7;i++) {
			myWorld.putTopWall(i, 0);
			myWorld.putLeftWall(0, i);
		}
		
		new SimpleBuggle(myWorld, "Walker", 0, 0, Direction.NORTH, Color.black, Color.red);

		setup(myWorld);
	}
}
