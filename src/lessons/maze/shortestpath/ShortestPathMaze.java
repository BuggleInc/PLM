package lessons.maze.shortestpath;

import java.io.IOException;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class ShortestPathMaze extends ExerciseTemplated {

	public ShortestPathMaze(Game game, Lesson lesson) throws IOException, BrokenWorldFileException {
		super(game, lesson);
		tabName = "JediEscaper";
				
		setup(new World[] {
				((BuggleWorld) BuggleWorld.newFromFile(game, "lessons/maze/shortestpath/WallFollowerMaze")).ignoreDirectionDifference(),
				((BuggleWorld) BuggleWorld.newFromFile(game, "lessons/maze/shortestpath/PledgeMaze")).ignoreDirectionDifference()
		});
	}
}
	