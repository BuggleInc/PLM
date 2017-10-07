package maze.pledge;

import java.io.IOException;

import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class PledgeMaze extends ExerciseTemplated {

	public PledgeMaze() throws IOException, BrokenWorldFileException {
		super("PledgeMaze", "PledgeMaze");
		tabName = "Escaper";
				
		setup( new World[] {
				((BuggleWorld) BuggleWorld.newFromFile("maze/pledge/PledgeMaze")).ignoreDirectionDifference(),
				((BuggleWorld) BuggleWorld.newFromFile("maze/pledge/PledgeMaze2")).ignoreDirectionDifference()
		});
	}
}
