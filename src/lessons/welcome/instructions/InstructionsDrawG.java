package lessons.welcome.instructions;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class InstructionsDrawG extends ExerciseTemplated {

	public InstructionsDrawG(Lesson lesson) {
		super(lesson);
		tabName = "Source";

		BuggleWorld myWorld = new BuggleWorld("Training World", 7, 7);
		new Buggle(myWorld, "Rookie", 5, 1, Direction.NORTH, Color.black, Color.blue);
		
		setup(myWorld);
	}
}
