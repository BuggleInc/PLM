package lessons.welcome.methods.flowerpot;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.bugglequest.BuggleWorld;

public class FlowerCase extends ExerciseTemplated {

	public FlowerCase(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		BuggleWorld[] myWorlds = new BuggleWorld[] {
				(BuggleWorld) BuggleWorld.newFromFile("lessons/welcome/methods/flowerpot/FlowerCase")
		};

		setup(myWorlds);
	}
}
