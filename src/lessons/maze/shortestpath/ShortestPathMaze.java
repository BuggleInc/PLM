package lessons.maze.shortestpath;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class ShortestPathMaze extends ExerciseTemplated {

	public ShortestPathMaze(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		tabName = "JediEscaper";
				
		setup(new World[] {
				((BuggleWorld) BuggleWorld.newFromFile("lessons/maze/shortestpath/WallFollowerMaze")).ignoreDirectionDifference(),
				((BuggleWorld) BuggleWorld.newFromFile("lessons/maze/shortestpath/PledgeMaze")).ignoreDirectionDifference()
		});
	}
}
