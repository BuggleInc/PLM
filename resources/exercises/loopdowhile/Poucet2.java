package loopdowhile;

import java.io.IOException;

import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class Poucet2 extends ExerciseTemplated {

	public Poucet2(FileUtils fileUtils) throws IOException, BrokenWorldFileException {
		super("Poucet2", "Poucet2");
		tabName = "Poucet";
		//setToolbox();

		/* Create initial situation */
		World[] myWorlds = new World[] {
				BuggleWorld.newFromFile(fileUtils, "loopdowhile/Poucet"),
				BuggleWorld.newFromFile(fileUtils, "loopdowhile/Poucet3"),
		};

		setup(myWorlds);
	}
}
