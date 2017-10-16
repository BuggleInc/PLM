package methods.flowerpot;

import java.io.IOException;

import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.bugglequest.BuggleWorld;

public class FlowerPot extends ExerciseTemplated {

	public FlowerPot(FileUtils fileUtils) throws IOException, BrokenWorldFileException {
		super("FlowerPot", "FlowerPot");
		//setToolbox();
		BuggleWorld[] myWorlds = new BuggleWorld[] {
				(BuggleWorld) BuggleWorld.newFromFile(fileUtils, "methods/flowerpot/FlowerPot")
		};

		setup(myWorlds);
	}
}
