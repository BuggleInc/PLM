package lessons.lightbot;

import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.lightbot.LightBotEntity;
import jlm.universe.lightbot.LightBotExercise;
import jlm.universe.lightbot.LightBotWorld;

public class Board03Jump extends LightBotExercise {

	public Board03Jump(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld("Board 3", 8, 8);
		
		new LightBotEntity(myWorld, "D2R2", 3, 0, Direction.SOUTH);
		
		for (int i=0;i<8;i++) 
			myWorld.setHeight(i,2,1);
		for (int i=3;i<6;i++) 
			myWorld.addLight(i,4);

		setup(myWorld);
	}
}
