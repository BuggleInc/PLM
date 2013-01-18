package lessons.lightbot;

import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.lightbot.LightBotEntity;
import jlm.universe.lightbot.LightBotExercise;
import jlm.universe.lightbot.LightBotWorld;

public class Board04Stairs extends LightBotExercise {

	public Board04Stairs(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld("Board 4", 8, 8);
		
		new LightBotEntity(myWorld, "D2R2", 1, 2, Direction.EAST);

		myWorld.setHeight(3,2,1);
		myWorld.setHeight(4,2,2);
		for (int i=2;i<8;i++)
			myWorld.setHeight(5,i,2);
		myWorld.addLight(5, 7);
		
		setup(myWorld);
	}
}
