package methods.flowerpot;

import java.io.IOException;

import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.BrokenWorldFileException;
import plm.universe.bugglequest.BuggleWorld;

public class FlowerPot extends ExerciseTemplated {

	public FlowerPot() throws IOException, BrokenWorldFileException {
		super("FlowerPot", "FlowerPot");
		//setToolbox();
		BuggleWorld[] myWorlds = new BuggleWorld[] {
				(BuggleWorld) BuggleWorld.newFromFile("methods/flowerpot/FlowerPot")
		};

		setup(myWorlds);
	}
}
