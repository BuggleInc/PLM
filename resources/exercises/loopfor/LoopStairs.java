package loopfor;

import java.io.IOException;

import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class LoopStairs extends ExerciseTemplated{

		public LoopStairs() throws IOException, BrokenWorldFileException {
			super("LoopStairs", "LoopStairs");
			tabName = "Runner";
			//setToolbox();

			/* Create initial situation */
			World[] myWorlds = new World[] {
					BuggleWorld.newFromFile("loopfor/LoopStairs")
			};

			setup(myWorlds);
		}

}
