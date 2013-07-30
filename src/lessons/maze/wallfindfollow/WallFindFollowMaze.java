package lessons.maze.wallfindfollow;

import java.io.IOException;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.BrokenWorldFileException;
import jlm.universe.World;
import jlm.universe.bugglequest.BuggleWorld;

public class WallFindFollowMaze extends ExerciseTemplated {

	public WallFindFollowMaze(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		tabName = "Escaper";
		nameOfCorrectionEntity = "lessons.maze.wallfollower.WallFollowerMazeEntity"; // Use the same entity than in previous exercise
		
		setup(new World[] {
				BuggleWorld.newFromFile("lessons/maze/wallfindfollow/WallFindFollowMaze"),
				BuggleWorld.newFromFile("lessons/maze/wallfindfollow/WallFindFollowMaze2")
				
		});
	}
}
