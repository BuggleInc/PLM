package lessons.lightbot;

import jlm.lesson.Lesson;
import universe.bugglequest.Direction;

public class Board1TwoSteps extends LightBotExercise {

	public Board1TwoSteps(Lesson lesson) {
		super(lesson);
		tabName = "Mars";
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld("Two steps", 8, 8);
		for (int i=0;i<8;i++) {
			myWorld.setHeight(0,i,2);
			myWorld.setHeight(7,i,2);
		}			
		myWorld.addLight(4,4);
		
		new LightBot(myWorld, "D2R2", 4, 2, Direction.SOUTH);
		newTextFile("main", "");
		newTextFile("function 1", "");
		newTextFile("function 2", "");
		setup(myWorld);
	}
}
