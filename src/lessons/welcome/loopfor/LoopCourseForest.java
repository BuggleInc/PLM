package lessons.welcome.loopfor;

import java.io.IOException;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.BrokenWorldFileException;
import jlm.universe.World;
import jlm.universe.bugglequest.BuggleWorld;

public class LoopCourseForest extends ExerciseTemplated{
	
		public LoopCourseForest(Lesson lesson) throws IOException, BrokenWorldFileException {
			super(lesson);
			tabName = "Runner";
					
			/* Create initial situation */
			World[] myWorlds = new World[] {
					BuggleWorld.newFromFile("lessons/welcome/loopfor/LoopCourseForest")
			};
			for (World w: myWorlds)
				w.setDelay(10); // runners are moving faster than usual
			
			setup(myWorlds);
		}

}
