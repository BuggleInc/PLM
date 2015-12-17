package lessons.lightbot;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.lightbot.LightBotEntity;
import plm.universe.lightbot.LightBotExercise;
import plm.universe.lightbot.LightBotWorld;

public class Board10Wall extends LightBotExercise {

	public Board10Wall(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld(game, "Board 10", 8, 8);
		
		new LightBotEntity(myWorld, "D2R2", 1, 0, Direction.SOUTH);

		for (int i=0;i<7;i++)
			myWorld.setHeight(2, i, 2);
		for (int i=3;i<8;i++)
			myWorld.setHeight(3, i, 2);
		myWorld.addLight(2, 0);
		myWorld.addLight(3, 7);
		myWorld.setHeight(1, 4, 1);
		myWorld.setHeight(2, 3, 4);
		myWorld.setHeight(3, 3, 4);
		myWorld.setHeight(2, 5, 3);
		myWorld.setHeight(3, 5, 4);
		myWorld.setHeight(3, 4, 3);
		
		setup(myWorld);
	}
}
