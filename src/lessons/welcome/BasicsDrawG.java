package lessons.welcome;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class BasicsDrawG extends ExerciseTemplated {

	public BasicsDrawG(Lesson lesson) {
		super(lesson);
		tabName = "SourceCode";

		BuggleWorld myWorld = new BuggleWorld("Training World", 7, 7);
		new Buggle(myWorld, "Rookie", 5, 1, Direction.NORTH, Color.black, Color.blue);
		
		setup(myWorld);
	}
}
