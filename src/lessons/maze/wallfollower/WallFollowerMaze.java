package lessons.maze.wallfollower;

import java.io.IOException;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.BrokenWorldFileException;
import jlm.universe.World;
import jlm.universe.bugglequest.BuggleWorld;

public class WallFollowerMaze extends ExerciseTemplated {

	public WallFollowerMaze(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		tabName = "Escaper";
				
		setup(new World[] {
				BuggleWorld.newFromFile("lessons/maze/wallfollower/WallFollowerMaze"),
				BuggleWorld.newFromFile("lessons/maze/wallfollower/WallFollowerMaze2")
		});
	}
}
