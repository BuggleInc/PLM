package summative.moria;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class Moria extends ExerciseTemplated {

	public Moria(FileUtils fileUtils) throws IOException, BrokenWorldFileException {
		super("Moria");
		tabName = "DwarfCode";

		/* Create initial situation */
		World[] myWorlds = new World[] {
				BuggleWorld.newFromFile(fileUtils, "summative/moria/Moria"),
		};

		setup(myWorlds);
	}

}
