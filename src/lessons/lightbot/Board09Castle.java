package lessons.lightbot;

import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.lightbot.LightBotEntity;
import jlm.universe.lightbot.LightBotExercise;
import jlm.universe.lightbot.LightBotWorld;

public class Board09Castle extends LightBotExercise {

	public Board09Castle(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld("Board 9", 8, 8);
		
		new LightBotEntity(myWorld, "D2R2", 2, 1, Direction.SOUTH);

		for (int i=1;i<7;i++) {
			myWorld.setHeight(3, i, 1);
			myWorld.setHeight(6, i, 1);
		}
		for (int i=2;i<8;i++) {
			myWorld.setHeight(i, 2, 1);
			myWorld.setHeight(i, 5, 1);
		}
		for (int i=2;i<6;i++) {
			myWorld.addLight(3, i);
			myWorld.addLight(6, i);
		}
		for (int i=3;i<6;i++) {
			myWorld.addLight(i, 2);
			myWorld.addLight(i, 5);
		}
		myWorld.setHeight(3, 2, 2);
		myWorld.setHeight(3, 5, 2);
		myWorld.setHeight(6, 2, 2);
		myWorld.setHeight(6, 5, 2);
		
		setup(myWorld);
	}
}
