package lessons.welcome.loopfor;

import java.io.IOException;
import java.util.Arrays;

import lessons.turmites.helloturmite.HelloTurmiteEntity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class LoopStairs extends ExerciseTemplated{
	
		public LoopStairs(Lesson lesson) throws IOException, BrokenWorldFileException {
			super(lesson);
			tabName = "Runner";
					
			/* Create initial situation */
			World[] myWorlds = new World[] {
					BuggleWorld.newFromFile("lessons/welcome/loopfor/LoopStairs")
			};

			myWorlds = Arrays.stream(myWorlds).map(s->s.replaceEntities(LoopStairsEntity::new)).toList().toArray(new World[0]);
			setup(myWorlds);
		}

}
