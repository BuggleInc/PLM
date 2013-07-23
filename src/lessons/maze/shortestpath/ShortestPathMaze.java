package lessons.maze.shortestpath;

import java.io.IOException;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.BrokenWorldFileException;
import jlm.universe.World;
import jlm.universe.bugglequest.BuggleWorld;

public class ShortestPathMaze extends ExerciseTemplated {

	public ShortestPathMaze(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		tabName = "JediEscaper";
				
		setup(new World[] {
				BuggleWorld.newFromFile("lessons/maze/shortestpath/WallFollowerMaze"),
				BuggleWorld.newFromFile("lessons/maze/shortestpath/PledgeMaze")
		});
	}
}
