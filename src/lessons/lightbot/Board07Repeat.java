package lessons.lightbot;

import jlm.lesson.Lesson;
import universe.bugglequest.Direction;

public class Board07Repeat extends LightBotExercise {

	public Board07Repeat(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld("Mars", 8, 8);
		
		new LightBot(myWorld, "D2R2", 4, 0, Direction.SOUTH);

		for (int i=1;i<8;i++) {
			myWorld.addLight(4, i);
			myWorld.addLight(3, i);
		}
		
		newTextFile("main", "");
		newTextFile("function 1", "");
		newTextFile("function 2", "");
		setup(myWorld);
	}
}
