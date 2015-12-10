package lessons.maze.island;

import java.io.IOException;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class IslandMaze extends ExerciseTemplated {

	public IslandMaze(Game game, Lesson lesson) throws IOException, BrokenWorldFileException {
		super(game, lesson);
		tabName = "Escaper";
				
		/* Create initial situation */
		setup( new World[] {
				((BuggleWorld) BuggleWorld.newFromFile(game, "lessons/maze/island/IslandMaze")).ignoreDirectionDifference(),
				((BuggleWorld) BuggleWorld.newFromFile(game, "lessons/maze/island/IslandMaze2")).ignoreDirectionDifference()
		});
	}
}
