package summative.moria;

import java.io.IOException;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class Moria extends ExerciseTemplated {

	public Moria() throws IOException, BrokenWorldFileException {
		super("Moria", "Moria");
		tabName = "DwarfCode";

		/* Create initial situation */
		World[] myWorlds = new World[] {
				BuggleWorld.newFromFile("summative/moria/Moria"),
		};

		setup(myWorlds);
	}

}
