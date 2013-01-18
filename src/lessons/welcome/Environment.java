package lessons.welcome;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class Environment extends ExerciseTemplated {
	public Environment(Lesson lesson) {
		super(lesson);
		tabName = "SourceCode";
		
		BuggleWorld myWorld = new BuggleWorld("Training Camp",7,7);
		new Buggle(myWorld, "Noob", 3, 3, Direction.NORTH, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
