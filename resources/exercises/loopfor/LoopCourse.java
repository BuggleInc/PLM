package loopfor;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class LoopCourse extends ExerciseTemplated{

		public LoopCourse(FileUtils fileUtils) throws IOException, BrokenWorldFileException {
			super("LoopCourse", "LoopCourse");
			tabName = "Runner";
			//setToolbox();

			/* Create initial situation */
			World[] myWorlds = new World[] {
					BuggleWorld.newFromFile(fileUtils, "loopfor/LoopCourse")
			};

			setup(myWorlds);
		}

}
