package lessons.welcome.methods.flowerpot;

import java.awt.Color;
import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class FlowerCase extends ExerciseTemplated {

	public FlowerCase(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		BuggleWorld[] myWorlds = new BuggleWorld[] {
				(BuggleWorld) BuggleWorld.newFromFile("lessons/welcome/methods/flowerpot/FlowerCase")
		};

		new SimpleBuggle(myWorlds[0], "Van Gogh", 1, 2, Direction.EAST, Color.black, Color.lightGray);
		
		setup(myWorlds);
	}
}
