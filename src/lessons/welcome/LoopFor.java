package lessons.welcome;

import java.awt.Color;

import jlm.bugglequest.Buggle;
import jlm.bugglequest.Direction;
import jlm.bugglequest.World;
import jlm.exception.AlreadyHaveBaggleException;

import lessons.ExerciseTemplated;
import lessons.Lesson;

public class LoopFor extends ExerciseTemplated {

	public LoopFor(Lesson lesson) {
		super(lesson);
		name = "Boucles pour";
		tabName = "Program";
		
		World myWorld = new World("Kitchen",7,7);
		for (int i=0;i<7;i++) {
			new Buggle(myWorld, "Ungry"+(i+1), i, 6, Direction.NORTH, Color.black, Color.lightGray);
		    
		    try {
				myWorld.getCell(i, 6-i).newBaggle();
			} catch (AlreadyHaveBaggleException e) {
				e.printStackTrace();
			}
		}
		
		setup(myWorld);
	}
}
