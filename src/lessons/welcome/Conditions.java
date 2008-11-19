package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;

import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;

public class Conditions extends ExerciseTemplated {

	public Conditions(Lesson lesson) {
		super(lesson);
		name = "Instructions conditionnelles";
		tabName = "Program";
				
		BuggleWorld myWorld = new BuggleWorld("Closed World",7,7);
		for (int i=0;i<7;i++) { 
			new Buggle(myWorld, "Buggle "+(i+1), i, 3, Direction.NORTH, Color.black, Color.lightGray);
			if (i%2 == 0) {
				myWorld.getCell(i, 3).putTopWall();
				myWorld.getCell(i, 5).putTopWall();
			} else {
				myWorld.getCell(i, 2).putTopWall();
				myWorld.getCell(i, 4).putTopWall();
			}
			if (i!=0) {
				myWorld.getCell(i, 2).putLeftWall();
				myWorld.getCell(i, 4).putLeftWall();
			}			
		}
		myWorld.getCell(0, 3).putLeftWall();
		myWorld.getCell(0, 4).putLeftWall();

		setup(myWorld);
	}
}
