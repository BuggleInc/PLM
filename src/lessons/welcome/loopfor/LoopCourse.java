package lessons.welcome.loopfor;

import java.io.IOException;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class LoopCourse extends ExerciseTemplated{
	
		public LoopCourse(Game game, Lesson lesson) throws IOException, BrokenWorldFileException {
			super(game, lesson);
			tabName = "Runner";
			setToolbox();
					
			/* Create initial situation */
			World[] myWorlds = new World[] {
					BuggleWorld.newFromFile(game, "lessons/welcome/loopfor/LoopCourse")
			};
			for (World w: myWorlds)
				w.setDelay(10); // runners are moving faster than usual
			
			setup(myWorlds);
		}

}
