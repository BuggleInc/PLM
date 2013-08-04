package lessons.welcome.variables;

import java.io.IOException;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.BrokenWorldFileException;
import jlm.universe.World;
import jlm.universe.bugglequest.BuggleWorld;

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