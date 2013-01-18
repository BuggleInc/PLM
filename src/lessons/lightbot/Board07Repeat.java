package lessons.lightbot;

import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.lightbot.LightBotEntity;
import jlm.universe.lightbot.LightBotExercise;
import jlm.universe.lightbot.LightBotWorld;

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
