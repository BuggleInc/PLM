package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.universe.Direction;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.exception.AlreadyHaveBaggleException;

public class LoopFor extends ExerciseTemplated {

	public LoopFor(Lesson lesson) {
		super(lesson);
		tabName = "Program";
		
		BuggleWorld myWorld = new BuggleWorld("Kitchen",7,7);
		for (int i=0;i<7;i++) {
			new Buggle(myWorld, "Ungry"+(i+1), i, 6, Direction.NORTH, Color.black, Color.lightGray);
		    
		    try {
				myWorld.newBaggle(i, 6-i);
			} catch (AlreadyHaveBaggleException e) {
				e.printStackTrace();
			}
		}
		
		setup(myWorld);
	}
}
