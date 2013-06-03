package lessons.lightbot;

import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.lightbot.LightBotEntity;
import jlm.universe.lightbot.LightBotExercise;
import jlm.universe.lightbot.LightBotWorld;

public class Board12Escher extends LightBotExercise {

	public Board12Escher(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld("Board 12", 8, 8);
		
		new LightBotEntity(myWorld, "D2R2", 0, 4, Direction.EAST);

		for (int i=1;i<5;i++) {
			myWorld.setHeight(5, i, i);  myWorld.addLight(5, i);
			myWorld.setHeight(5, 8-i, i);myWorld.addLight(5, 8-i);
			myWorld.setHeight(6-i, 7, i);myWorld.addLight(6-i, 7);
			myWorld.setHeight(6-i, 1, i);myWorld.addLight(6-i, 1);
			myWorld.setHeight(i+1, 4, i);myWorld.addLight(i+1, 4);
			myWorld.setHeight(2, 5-i, i);myWorld.addLight(2, 5-i);
			myWorld.setHeight(2, 3+i, i);myWorld.addLight(2, 3+i);
		}
		
		setup(myWorld);
	}
}
