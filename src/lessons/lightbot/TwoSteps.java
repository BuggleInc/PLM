package lessons.lightbot;

import jlm.lesson.Lesson;
import universe.bugglequest.Direction;

public class TwoSteps extends LightBotExercise {

	public TwoSteps(Lesson lesson) {
		super(lesson);
		tabName = "TwoSteps";
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld("Two steps", 8, 8);
		for (int i=0;i<8;i++) {
			myWorld.setHeight(0,i,2);
			myWorld.setHeight(7,i,2);
		}			
		myWorld.addLight(2,5);
		
		new LightBot(myWorld, "D2R2", 2, 3, Direction.SOUTH);
		newTextFile("main", "forward\nforward\nlight");
		setup(myWorld);
	}
}
