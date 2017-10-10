package lessons.lightbot;

import lessons.lightbot.universe.LightBotEntity;
import lessons.lightbot.universe.LightBotExercise;
import lessons.lightbot.universe.LightBotWorld;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;

public class Board01TwoSteps extends LightBotExercise {

	public Board01TwoSteps(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld("Board 1", 8, 8);
		for (int i=0;i<8;i++) {
			myWorld.setHeight(0,i,2);
			myWorld.setHeight(7,i,2);
		}			
		myWorld.addLight(4,4);
		
		new LightBotEntity(myWorld, "D2R2", 4, 2, Direction.SOUTH);
		setup(myWorld);
	}
}
