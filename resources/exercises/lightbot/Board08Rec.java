package lessons.lightbot;

import lessons.lightbot.universe.LightBotEntity;
import lessons.lightbot.universe.LightBotExercise;
import lessons.lightbot.universe.LightBotWorld;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;

public class Board08Rec extends LightBotExercise {

	public Board08Rec(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld("Board 8", 8, 8);
		
		new LightBotEntity(myWorld, "D2R2", 2, 2, Direction.EAST);

		for (int i=2;i<7;i++) {
			myWorld.setHeight(i, 2, 1);
			myWorld.setHeight(i, 4, 1);
			myWorld.setHeight(i, 6, 1);
			if (i%2==0) {
				myWorld.setHeight(i, 3, 1);
				myWorld.setHeight(i, 5, 1);				
			}
		}
		myWorld.setHeight(2, 2, 0);
		myWorld.setHeight(2, 3, 0);
		myWorld.addLight(4, 2);
		myWorld.addLight(6, 2);
		myWorld.addLight(2, 4);
		myWorld.addLight(4, 4);
		myWorld.addLight(6, 4);
		myWorld.addLight(2, 6);
		myWorld.addLight(4, 6);
		myWorld.addLight(6, 6);
		
		setup(myWorld);
	}
}
