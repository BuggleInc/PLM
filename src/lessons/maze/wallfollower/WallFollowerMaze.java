package lessons.maze.wallfollower;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

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
