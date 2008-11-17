package lessons.traversal;

import java.awt.Color;

import lessons.ExerciseTemplated;
import lessons.Lesson;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;

public class ZigZag extends ExerciseTemplated {

	public ZigZag(Lesson lesson) {
		super(lesson);
		name = "Parcours en zigzag";
		tabName = "ZigZag";

		BuggleWorld myWorld = new BuggleWorld("Grid",7,7);
		for (int i=0; i<7;i++) {
			myWorld.getCell(i, 0).putTopWall();
			myWorld.getCell(0, i).putLeftWall();
		}
		
		new Buggle(myWorld, "Walker", 0, 0, Direction.NORTH, Color.black, Color.red);

		setup(myWorld);
	}
}
