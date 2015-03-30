package lessons.maze.wallfindfollow;

import java.io.IOException;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class WallFindFollowMaze extends ExerciseTemplated {

	public WallFindFollowMaze(Game game, Lesson lesson) throws IOException, BrokenWorldFileException {
		super(game, lesson);
		tabName = "Escaper";
		
		setup(new World[] {
				((BuggleWorld) BuggleWorld.newFromFile(game, "lessons/maze/wallfindfollow/WallFindFollowMaze")).ignoreDirectionDifference(),
				((BuggleWorld) BuggleWorld.newFromFile(game, "lessons/maze/wallfindfollow/WallFindFollowMaze2")).ignoreDirectionDifference()
				
		});
	}
}
