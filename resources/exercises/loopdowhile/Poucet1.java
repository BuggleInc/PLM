package loopdowhile;

import java.io.IOException;

import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class Poucet1 extends ExerciseTemplated {

	public Poucet1(FileUtils fileUtils) throws IOException, BrokenWorldFileException {
		super("Poucet1", "Poucet1");
		tabName = "Poucet";

		/* Create initial situation */
		World[] myWorlds = new World[] {
				BuggleWorld.newFromFile(fileUtils,"loopdowhile/Poucet"),
				BuggleWorld.newFromFile(fileUtils,"loopdowhile/Poucet2"),
		};
		setup(myWorlds);
	}
}
