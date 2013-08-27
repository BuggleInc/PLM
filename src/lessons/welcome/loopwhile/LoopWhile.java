package lessons.welcome.loopwhile;

import java.awt.Color;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.Buggle;
import plm.universe.bugglequest.BuggleWorld;

public class LoopWhile extends ExerciseTemplated {

	public LoopWhile(Lesson lesson) {
		super(lesson);
		tabName = "Program";

		BuggleWorld myWorld = new BuggleWorld("Closed world",7,7);
		for (int i=0;i<7;i++) {
			new Buggle(myWorld, "Joker "+(i+1), i, 6, Direction.NORTH, Color.black, Color.lightGray);
			myWorld.putTopWall (i, 6-i);
			myWorld.putLeftWall(i, 6-i);
			myWorld.putLeftWall(0, i  );
			myWorld.putTopWall (i, 0  );
		}
		
		setup(myWorld);
	}
}
