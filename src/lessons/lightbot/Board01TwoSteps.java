package lessons.lightbot;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.lightbot.LightBotEntity;
import plm.universe.lightbot.LightBotExercise;
import plm.universe.lightbot.LightBotWorld;

public class Board01TwoSteps extends LightBotExercise {

	public Board01TwoSteps(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld(game, "Board 1", 8, 8);
		for (int i=0;i<8;i++) {
			myWorld.setHeight(0,i,2);
			myWorld.setHeight(7,i,2);
		}			
		myWorld.addLight(4,4);
		
		new LightBotEntity(myWorld, "D2R2", 4, 2, Direction.SOUTH);
		setup(myWorld);
	}
}
