package lessons.lightbot;

import jlm.lesson.Lesson;
import universe.bugglequest.Direction;

public class Board2Turn extends LightBotExercise {

	public Board2Turn(Lesson lesson) {
		super(lesson);
		tabName = "Mars";
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld("Two steps", 8, 8);
		myWorld.setHeight(2, 4, 2);
		myWorld.addLight(2,6);
		
		new LightBot(myWorld, "D2R2", 2, 2, Direction.SOUTH);
		
		/* decorum */
		for (int i=2;i<6;i++) {
			myWorld.setHeight(0,i,1);
			myWorld.setHeight(7,i,1);
		}			
		myWorld.setHeight(0, 0, 3);
		myWorld.setHeight(7, 0, 3);
		myWorld.setHeight(0, 7, 3);
		myWorld.setHeight(7, 7, 3);

		myWorld.setHeight(0, 1, 2);
		myWorld.setHeight(7, 1, 2);
		myWorld.setHeight(0, 6, 2);
		myWorld.setHeight(7, 6, 2);

		
		newTextFile("main", "");
		newTextFile("function 1", "");
		newTextFile("function 2", "");
		setup(myWorld);
	}
}
