package lessons.welcome.traversal;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class Snake extends ExerciseTemplated {

	public Snake(Game game, Lesson lesson) {
		super(game, lesson);
		tabName = "SnakeBuggle";
		setToolbox();

		BuggleWorld myWorld = new BuggleWorld(game, "Desert",7,7);
		for (int i=0; i<7;i++) {
			myWorld.putTopWall(i, 0);
			myWorld.putLeftWall(0, i);
		}
		
		new SimpleBuggle(myWorld, "Snake", 0, 6, Direction.EAST, Color.red, Color.red);

		setup(myWorld);
	}
}
