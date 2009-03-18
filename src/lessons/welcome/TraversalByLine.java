package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.universe.Direction;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;

public class TraversalByLine extends ExerciseTemplated {

	public TraversalByLine(Lesson lesson) {
		super(lesson);
		tabName = "LineByLine";

		BuggleWorld myWorld = new BuggleWorld("Grid",7,7);
		for (int i=0; i<7;i++) {
			myWorld.putTopWall(i, 0);
			myWorld.putLeftWall(0, i);
		}
		
		new Buggle(myWorld, "Walker", 0, 0, Direction.NORTH, Color.black, Color.red);

		setup(myWorld);
	}
}
