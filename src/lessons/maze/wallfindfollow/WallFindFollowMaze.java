package lessons.maze.wallfindfollow;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class WallFindFollowMaze extends ExerciseTemplated {

	public WallFindFollowMaze(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		tabName = "Escaper";
		
		setup(new World[] {
				BuggleWorld.newFromFile("lessons/maze/wallfindfollow/WallFindFollowMaze"),
				BuggleWorld.newFromFile("lessons/maze/wallfindfollow/WallFindFollowMaze2")
				
		});
	}
}
