package lessons.lightbot.boards;

import jlm.lesson.Lesson;
import jlm.universe.Direction;
import lessons.lightbot.world.LightBotEntity;
import lessons.lightbot.world.LightBotExercise;
import lessons.lightbot.world.LightBotWorld;

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
