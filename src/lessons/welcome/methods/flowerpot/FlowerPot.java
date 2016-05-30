package lessons.welcome.methods.flowerpot;

import java.io.IOException;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.bugglequest.BuggleWorld;

public class FlowerPot extends ExerciseTemplated {

	public FlowerPot(Game game, Lesson lesson) throws IOException, BrokenWorldFileException {
		super(game, lesson);
		setToolbox();
		BuggleWorld[] myWorlds = new BuggleWorld[] {
				(BuggleWorld) BuggleWorld.newFromFile(game, "lessons/welcome/methods/flowerpot/FlowerPot")
		};

		setup(myWorlds);
	}
}
