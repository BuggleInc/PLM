package lessons.welcome.methods.flowerpot;

import java.awt.Color;
import java.io.IOException;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.BrokenWorldFileException;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class FlowerCase extends ExerciseTemplated {

	public FlowerCase(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		BuggleWorld[] myWorlds = new BuggleWorld[] {
				(BuggleWorld) BuggleWorld.newFromFile("lessons/welcome/methods/flowerpot/FlowerCase")
		};

		new Buggle(myWorlds[0], "Van Gogh", 1, 2, Direction.EAST, Color.black, Color.lightGray);
		
		setup(myWorlds);
	}
}
