package lessons.lightbot.mission5;

import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.lightbot.LightBotEntity;
import jlm.universe.lightbot.LightBotExercise;
import jlm.universe.lightbot.LightBotWorld;

public class Board05Higher extends LightBotExercise {

	public Board05Higher(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld("Board 5", 8, 8);
		
		new LightBotEntity(myWorld, "D2R2", 2, 3, Direction.SOUTH);

		myWorld.setHeight(2,4,1);
		myWorld.setHeight(2,5,1);
		for (int i=2;i<6;i++)
			myWorld.setHeight(i,6,1);
		myWorld.setHeight(5,5,2);
		myWorld.setHeight(5,4,3);
		myWorld.setHeight(5,3,4);
		myWorld.addLight( 5,3);
		
		setup(myWorld);
	}
}
