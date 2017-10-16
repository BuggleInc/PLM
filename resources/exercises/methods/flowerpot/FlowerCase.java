package methods.flowerpot;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.bugglequest.BuggleWorld;

public class FlowerCase extends ExerciseTemplated {

	public FlowerCase(FileUtils fileUtils) throws IOException, BrokenWorldFileException {
		super("FlowerCase", "FlowerCase");
		//setToolbox();
		BuggleWorld[] myWorlds = new BuggleWorld[] {
				(BuggleWorld) BuggleWorld.newFromFile(fileUtils, "methods/flowerpot/FlowerCase")
		};

		setup(myWorlds);
	}
}
