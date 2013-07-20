package lessons.welcome.loop.dowhileloop;

import java.io.IOException;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.BrokenWorldFileException;
import jlm.universe.World;
import jlm.universe.bugglequest.BuggleWorld;

public class Poucet extends ExerciseTemplated {

	public Poucet(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		tabName = "Poucet";
		
		/* Create initial situation */
		World[] myWorlds = new World[] {
				BuggleWorld.newFromFile("lessons/welcome/loop/dowhileloop/poucet"),
				BuggleWorld.newFromFile("lessons/welcome/loop/dowhileloop/poucet2"),
		};
		for (World w: myWorlds)
			w.setDelay(50); // moving a bit faster than usual
		
		setup(myWorlds);
	}
}