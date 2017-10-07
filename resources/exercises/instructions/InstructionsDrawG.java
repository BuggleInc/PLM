package instructions;

import java.awt.Color;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class InstructionsDrawG extends ExerciseTemplated {

	public InstructionsDrawG() {
		super("InstructionsDrawG", "InstructionsDrawG");
		tabName = "Source";
		//setToolbox();

		BuggleWorld myWorld = new BuggleWorld("Training World", 7, 7);
		new SimpleBuggle(myWorld, "Rookie", 5, 1, Direction.NORTH, Color.black, Color.blue);
		
		setup(myWorld);
	}
}
