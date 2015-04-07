package lessons.lightbot;

import lessons.lightbot.universe.LightBotEntity;
import lessons.lightbot.universe.LightBotExercise;
import lessons.lightbot.universe.LightBotWorld;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;

public class Board07Repeat extends LightBotExercise {

	public Board07Repeat(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld(game, "Board 7", 8, 8);
		
		new LightBotEntity(myWorld, "D2R2", 4, 0, Direction.SOUTH);

		for (int i=1;i<8;i++) {
			myWorld.addLight(4, i);
			myWorld.addLight(3, i);
		}
		
		setup(myWorld);
	}
}
