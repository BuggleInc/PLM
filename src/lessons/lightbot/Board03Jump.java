package lessons.lightbot;

import jlm.lesson.Lesson;
import universe.bugglequest.Direction;

public class Board03Jump extends LightBotExercise {

	public Board03Jump(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld("Mars", 8, 8);
		
		new LightBot(myWorld, "D2R2", 3, 0, Direction.SOUTH);
		
		for (int i=0;i<8;i++) 
			myWorld.setHeight(i,2,1);
		for (int i=3;i<6;i++) 
			myWorld.addLight(i,4);

		
		newTextFile("main", "");
		newTextFile("function 1", "");
		newTextFile("function 2", "");
		setup(myWorld);
	}
}
