package lessons.welcome.loopfor;

import java.io.IOException;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class LoopStairs extends ExerciseTemplated{
	
		public LoopStairs(Game game, Lesson lesson) throws IOException, BrokenWorldFileException {
			super(game, lesson);
			tabName = "Runner";
			setToolbox();
					
			/* Create initial situation */
			World[] myWorlds = new World[] {
					BuggleWorld.newFromFile(game, "lessons/welcome/loopfor/LoopStairs")
			};
			
			setup(myWorlds);
		}

}
