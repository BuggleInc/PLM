package loopfor;

import java.io.IOException;

import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class LoopCourseForest extends ExerciseTemplated{

		public LoopCourseForest() throws IOException, BrokenWorldFileException {
			super("LoopCourseForest", "LoopCourseForest");
			tabName = "Runner";
			//setToolbox();

			/* Create initial situation */
			World[] myWorlds = new World[] {
					BuggleWorld.newFromFile("loopfor/LoopCourseForest")
			};

			setup(myWorlds);
		}

}
