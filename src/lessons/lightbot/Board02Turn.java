package lessons.lightbot;

import jlm.lesson.Lesson;
import universe.bugglequest.Direction;

public class Board02Turn extends LightBotExercise {

	public Board02Turn(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld("Mars", 8, 8);
		myWorld.setHeight(4, 4, 2);
		myWorld.addLight(4,6);
		
		new LightBotEntity(myWorld, "D2R2", 4, 2, Direction.SOUTH);
		
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

		setup(myWorld);
	}
}
