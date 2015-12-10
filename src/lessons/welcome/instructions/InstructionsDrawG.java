package lessons.welcome.instructions;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class InstructionsDrawG extends ExerciseTemplated {

	public InstructionsDrawG(Game game, Lesson lesson) {
		super(game, lesson);
		tabName = "Source";
		setToolbox();

		BuggleWorld myWorld = new BuggleWorld(game, "Training World", 7, 7);
		new SimpleBuggle(myWorld, "Rookie", 5, 1, Direction.NORTH, Color.black, Color.blue);
		
		setup(myWorld);
	}
}
