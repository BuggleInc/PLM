package variables;

import java.io.IOException;

import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class RunFour extends ExerciseTemplated {

	public RunFour() throws IOException, BrokenWorldFileException {
		super("RunFour", "RunFour");
		//setToolbox();

		/* Create initial situation */
		World[] myWorlds = new World[] {
				BuggleWorld.newFromFile("variables/RunFour"),
		};

		setup(myWorlds);
	}
}
