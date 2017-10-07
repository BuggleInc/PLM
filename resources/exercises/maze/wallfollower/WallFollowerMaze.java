package maze.wallfollower;

import java.io.IOException;

import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class WallFollowerMaze extends ExerciseTemplated {

	public WallFollowerMaze() throws IOException, BrokenWorldFileException {
		super("WallFollowerMaze", "WallFollowerMaze");
		tabName = "Escaper";
				
		setup(new World[] {
				((BuggleWorld) BuggleWorld.newFromFile("maze/wallfollower/WallFollowerMaze")).ignoreDirectionDifference(),
				((BuggleWorld) BuggleWorld.newFromFile("maze/wallfollower/WallFollowerMaze2")).ignoreDirectionDifference()
		});
	}
}
