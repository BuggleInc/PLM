package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;

public class LoopDoWhile extends ExerciseTemplated {

	public LoopDoWhile(Lesson lesson) {
		super(lesson);
		tabName = "Program";
				
		BuggleWorld myWorld = new BuggleWorld("Yellow Submarine",7,7);
		for (int i=0;i<7;i++) {
			new Buggle(myWorld, "Beatles"+(i+1), i, 6, Direction.NORTH, Color.black, Color.lightGray);
		    for (int j=6; j>i; j--)
		    	myWorld.getCell(i, j).setColor(Color.yellow);
		}
    	myWorld.getCell(6, 6).setColor(Color.yellow);

    	setup(myWorld);
	}
}
