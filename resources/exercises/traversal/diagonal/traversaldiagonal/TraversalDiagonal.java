package traversal.diagonal.traversaldiagonal;

import java.awt.Color;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class TraversalDiagonal extends ExerciseTemplated {

	public TraversalDiagonal() {
		super("TraversalDiagonal", "TraversalDiagonal");
		tabName = "Diagonal";

		BuggleWorld myWorld = new BuggleWorld("Grid",7,7);
		for (int i=0; i<7;i++) {
			myWorld.putTopWall(i, 0);
			myWorld.putLeftWall(0, i);
		}
		
		new SimpleBuggle(myWorld, "Walker", 0, 0, Direction.NORTH, Color.black, Color.red);

		setup(myWorld);
	}
}
