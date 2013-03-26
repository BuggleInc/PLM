package lessons.welcome;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class Snake extends ExerciseTemplated {

	public Snake(Lesson lesson) {
		super(lesson);
		tabName = "SnakeBuggle";

		BuggleWorld myWorld = new BuggleWorld("Desert",7,7);
		for (int i=0; i<7;i++) {
			myWorld.putTopWall(i, 0);
			myWorld.putLeftWall(0, i);
		}
		
		new Buggle(myWorld, "Snake", 0, 6, Direction.EAST, Color.red, Color.red);

		setup(myWorld);
	}
}
