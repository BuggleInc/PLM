package lessons.welcome.loopdowhile;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class Poucet extends ExerciseTemplated {

	public Poucet(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		tabName = "Poucet";
		
		/* Create initial situation */
		World[] myWorlds = new World[] {
				BuggleWorld.newFromFile("lessons/welcome/loopdowhile/Poucet"),
				BuggleWorld.newFromFile("lessons/welcome/loopdowhile/Poucet2"),
		};
		for (World w: myWorlds)
			w.setDelay(50); // moving a bit faster than usual
		
		setup(myWorlds);
	}
}