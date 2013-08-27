package lessons.welcome.variables;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class RunHalf extends ExerciseTemplated {

	public RunHalf(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		
		World[] myWorlds = new World[] {
				BuggleWorld.newFromFile("lessons/welcome/variables/RunHalf"),
		};
		for (World w: myWorlds)
			w.setDelay(50); // moving a bit faster than usual
		
		setup(myWorlds);
	}
}