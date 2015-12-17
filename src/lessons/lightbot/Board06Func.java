package lessons.lightbot;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.lightbot.LightBotEntity;
import plm.universe.lightbot.LightBotExercise;
import plm.universe.lightbot.LightBotWorld;

public class Board06Func extends LightBotExercise {

	public Board06Func(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld(game, "Board 6", 8, 8);
		
		new LightBotEntity(myWorld, "D2R2", 1, 2, Direction.EAST);

		myWorld.setHeight(3, 2, 1);
		myWorld.setHeight(4, 2, 2);
		myWorld.setHeight(4, 3, 3);
		myWorld.setHeight(4, 4, 4);
		myWorld.addLight( 4, 4);
		for (int i=1;i<8;i++)
			myWorld.setHeight(i, 5, 2);
		myWorld.addLight(1, 5);
		myWorld.addLight(7, 5);
		
		setup(myWorld);
	}
}
