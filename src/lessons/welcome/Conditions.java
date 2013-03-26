package lessons.welcome;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class Conditions extends ExerciseTemplated {

	public Conditions(Lesson lesson) {
		super(lesson);
		tabName = "Program";
				
		BuggleWorld myWorld = new BuggleWorld("Closed World",7,7);
		for (int i=0;i<7;i++) { 
			new Buggle(myWorld, "Buggle "+(i+1), i, 3, Direction.NORTH, Color.black, Color.lightGray);
			if (i%2 == 0) {
				myWorld.putTopWall(i, 3);
				myWorld.putTopWall(i, 5);
			} else {
				myWorld.putTopWall(i, 2);
				myWorld.putTopWall(i, 4);
			}				         
			if (i!=0) {			         
			    myWorld.putLeftWall(i, 2);
			    myWorld.putLeftWall(i, 4);
			}			
		}
		myWorld.putLeftWall(0, 3);
		myWorld.putLeftWall(0, 4);

		setup(myWorld);
	}
}
