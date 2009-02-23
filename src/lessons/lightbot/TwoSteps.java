package lessons.lightbot;

import jlm.lesson.Lesson;
import universe.bugglequest.Direction;

public class TwoSteps extends LightBotExercise {

	public TwoSteps(Lesson lesson) {
		super(lesson);
		tabName = "TwoSteps";
				
		/* Create initial situation */
		LightBotWorld myWorld = new LightBotWorld("Two steps", 8, 8);

		new LightBot(myWorld, "D2R2", 2, 3, Direction.NORTH);
		setup(myWorld);
	}
}
