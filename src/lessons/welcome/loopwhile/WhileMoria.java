package lessons.welcome.loopwhile;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class WhileMoria extends ExerciseTemplated {

	public WhileMoria(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		tabName = "Balin";
		
		/* Create initial situation */
		World[] myWorlds = new World[] {
				BuggleWorld.newFromFile("lessons/welcome/loopwhile/WhileMoria"),
		};
		for (World w: myWorlds)
			w.setDelay(50); // moving a bit faster than usual
		
		setup(myWorlds);
	}
}
