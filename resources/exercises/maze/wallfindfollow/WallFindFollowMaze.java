package maze.wallfindfollow;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

import java.io.IOException;

public class WallFindFollowMaze extends ExerciseTemplated {

    public WallFindFollowMaze() throws IOException, BrokenWorldFileException {
        super("WallFindFollowMaze", "WallFindFollowMaze");
        tabName = "Escaper";

        setup(new World[]{
                ((BuggleWorld) BuggleWorld.newFromFile("maze/wallfindfollow/WallFindFollowMaze")).ignoreDirectionDifference(),
                ((BuggleWorld) BuggleWorld.newFromFile("maze/wallfindfollow/WallFindFollowMaze2")).ignoreDirectionDifference()
        });
    }
}
