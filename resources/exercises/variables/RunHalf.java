package variables;

import java.io.IOException;

import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class RunHalf extends ExerciseTemplated {

	public RunHalf() throws IOException, BrokenWorldFileException {
		super("RunHalf", "RunHalf");
		//setToolbox();

		World[] myWorlds = new World[] {
				BuggleWorld.newFromFile("variables/RunHalf"),
		};

		setup(myWorlds);
	}
}
