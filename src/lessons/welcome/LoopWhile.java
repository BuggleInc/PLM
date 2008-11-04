package lessons.welcome;

import java.awt.Color;

import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;


import lessons.ExerciseTemplated;
import lessons.Lesson;

public class LoopWhile extends ExerciseTemplated {

	public LoopWhile(Lesson lesson) {
		super(lesson);
		name = "Boucles tant que";
		tabName = "Program";

		BuggleWorld myWorld = new BuggleWorld("Closed world",7,7);
		for (int i=0;i<7;i++) {
			new Buggle(myWorld, "Joker "+(i+1), i, 6, Direction.NORTH, Color.black, Color.lightGray);
			myWorld.getCell(i, 6-i).putTopWall();
			myWorld.getCell(i, 6-i).putLeftWall();
			myWorld.getCell(0, i).putLeftWall();
			myWorld.getCell(i, 0).putTopWall();
		}
		
		setup(myWorld);
	}
}
