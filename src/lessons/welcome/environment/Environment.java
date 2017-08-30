package lessons.welcome.environment;

import java.awt.Color;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class Environment extends ExerciseTemplated {
	public Environment(Lesson lesson) {
		super(lesson);
		tabName = "Code";
		
		BuggleWorld myWorld = new BuggleWorld("Training Camp",7,7);
		new SimpleBuggle(myWorld, "Noob", 3, 3, Direction.NORTH, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
