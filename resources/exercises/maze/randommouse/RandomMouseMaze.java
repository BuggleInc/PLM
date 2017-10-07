package maze.randommouse;

import java.io.IOException;

import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class RandomMouseMaze extends ExerciseTemplated {

	public RandomMouseMaze() throws IOException, BrokenWorldFileException {
		super("RandomMouseMaze", "RandomMouseMaze");
		tabName = "RandomMouseMaze";

		setup( new World[] {
				((BuggleWorld) BuggleWorld.newFromFile("maze/randommouse/RandomMouseMaze")).ignoreDirectionDifference(),
				((BuggleWorld) BuggleWorld.newFromFile("maze/randommouse/RandomMouseMaze2")).ignoreDirectionDifference()
		});
	}
}
