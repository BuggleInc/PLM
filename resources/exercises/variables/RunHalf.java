package variables;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class RunHalf extends ExerciseTemplated {

	public RunHalf(FileUtils fileUtils) throws IOException, BrokenWorldFileException {
		super("RunHalf", "RunHalf");
		//setToolbox();

		World[] myWorlds = new World[] {
				BuggleWorld.newFromFile(fileUtils, "variables/RunHalf"),
		};

		setup(myWorlds);
	}
}
