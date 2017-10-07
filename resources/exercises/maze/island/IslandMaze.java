package maze.island;

import java.io.IOException;

import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class IslandMaze extends ExerciseTemplated {

	public IslandMaze() throws IOException, BrokenWorldFileException {
		super("IslandMaze", "IslandMaze");
		tabName = "Escaper";
				
		/* Create initial situation */
		setup( new World[] {
				((BuggleWorld) BuggleWorld.newFromFile("maze/island/IslandMaze")).ignoreDirectionDifference(),
				((BuggleWorld) BuggleWorld.newFromFile("maze/island/IslandMaze2")).ignoreDirectionDifference()
		});
	}
}
