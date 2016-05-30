package lessons.lightbot;

import lessons.lightbot.universe.LightBotEntity;
import lessons.lightbot.universe.LightBotExercise;
import lessons.lightbot.universe.LightBotWorld;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;

public class Board03Jump extends LightBotExercise {

	public Board03Jump(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld(game, "Board 3", 8, 8);
		
		new LightBotEntity(myWorld, "D2R2", 3, 0, Direction.SOUTH);
		
		for (int i=0;i<8;i++) 
			myWorld.setHeight(i,2,1);
		for (int i=3;i<6;i++) 
			myWorld.addLight(i,4);

		setup(myWorld);
	}
}
