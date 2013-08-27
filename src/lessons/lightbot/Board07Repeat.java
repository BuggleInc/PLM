package lessons.lightbot;

import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.lightbot.LightBotEntity;
import plm.universe.lightbot.LightBotExercise;
import plm.universe.lightbot.LightBotWorld;

public class Board07Repeat extends LightBotExercise {

	public Board07Repeat(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld("Board 7", 8, 8);
		
		new LightBotEntity(myWorld, "D2R2", 4, 0, Direction.SOUTH);

		for (int i=1;i<8;i++) {
			myWorld.addLight(4, i);
			myWorld.addLight(3, i);
		}
		
		setup(myWorld);
	}
}
