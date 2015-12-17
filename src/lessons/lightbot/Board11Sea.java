package lessons.lightbot;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.lightbot.LightBotEntity;
import plm.universe.lightbot.LightBotExercise;
import plm.universe.lightbot.LightBotWorld;

public class Board11Sea extends LightBotExercise {

	public Board11Sea(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld(game, "Board 11", 8, 8);
		
		new LightBotEntity(myWorld, "D2R2", 0, 0, Direction.SOUTH);

		for (int i=0;i<8;i++) {
			myWorld.setHeight(1, i, 1);
			myWorld.setHeight(3, i, 1);
			myWorld.setHeight(5, i, 1);
			myWorld.setHeight(6, i, 2);
			myWorld.setHeight(7, i, 3);
			for (int j=0;j<6;j++) {
				myWorld.addLight(j, i);
			}
		}
		myWorld.removeLight(0,0);
		
		setup(myWorld);
	}
}
