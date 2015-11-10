package lessons.welcome.traversal.line;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class TraversalByLine extends ExerciseTemplated {

	public TraversalByLine(Game game, Lesson lesson) {
		super(game, lesson);
		tabName = "LineByLine";
		setToolbox();

		BuggleWorld myWorld = new BuggleWorld(game, "Grid",7,7);
		for (int i=0; i<7;i++) {
			myWorld.putTopWall(i, 0);
			myWorld.putLeftWall(0, i);
		}
		
		new SimpleBuggle(myWorld, "Walker", 0, 0, Direction.NORTH, Color.black, Color.red);

		setup(myWorld);
	}
}
