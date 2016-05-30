package lessons.welcome.loopdowhile;

import java.io.IOException;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class Poucet2 extends ExerciseTemplated {

	public Poucet2(Game game, Lesson lesson) throws IOException, BrokenWorldFileException {
		super(game, lesson);
		tabName = "Poucet";
		setToolbox();
		
		/* Create initial situation */
		World[] myWorlds = new World[] {
				BuggleWorld.newFromFile(game, "lessons/welcome/loopdowhile/Poucet"),
				BuggleWorld.newFromFile(game, "lessons/welcome/loopdowhile/Poucet3"),
		};
		for (World w: myWorlds)
			w.setDelay(50); // moving a bit faster than usual
		
		setup(myWorlds);
	}
}