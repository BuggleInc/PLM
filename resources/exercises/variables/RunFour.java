package variables;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class RunFour extends ExerciseTemplated {

	public RunFour(FileUtils fileUtils) throws IOException, BrokenWorldFileException {
		super("RunFour");
		//setToolbox();

		/* Create initial situation */
		World[] myWorlds = new World[] {
				BuggleWorld.newFromFile(fileUtils, "variables/RunFour"),
		};

		setup(myWorlds);
	}
}
