package lessons.maze.pledge;

import java.io.IOException;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class PledgeMaze extends ExerciseTemplated {

	public PledgeMaze(Game game, Lesson lesson) throws IOException, BrokenWorldFileException {
		super(game, lesson);
		tabName = "Escaper";
				
		setup( new World[] {
				((BuggleWorld) BuggleWorld.newFromFile(game, "lessons/maze/pledge/PledgeMaze")).ignoreDirectionDifference(),	
				((BuggleWorld) BuggleWorld.newFromFile(game, "lessons/maze/pledge/PledgeMaze2")).ignoreDirectionDifference()
		});
	}
}
