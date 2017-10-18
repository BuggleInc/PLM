package loopfor;

import java.io.IOException;

import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class LoopStairs extends ExerciseTemplated{

		public LoopStairs(FileUtils fileUtils) throws IOException, BrokenWorldFileException {
			super("LoopStairs");
			tabName = "Runner";
			//setToolbox();

			/* Create initial situation */
			World[] myWorlds = new World[] {
					BuggleWorld.newFromFile(fileUtils, "loopfor/LoopStairs")
			};

			setup(myWorlds);
		}

}
