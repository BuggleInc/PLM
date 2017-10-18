package maze.wallfindfollow;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

import java.io.IOException;

public class WallFindFollowMaze extends ExerciseTemplated {

    public WallFindFollowMaze(FileUtils fileUtils) throws IOException, BrokenWorldFileException {
        super("WallFindFollowMaze");
        tabName = "Escaper";

        setup(new World[]{
                ((BuggleWorld) BuggleWorld.newFromFile(fileUtils, "maze/wallfindfollow/WallFindFollowMaze")).ignoreDirectionDifference(),
                ((BuggleWorld) BuggleWorld.newFromFile(fileUtils, "maze/wallfindfollow/WallFindFollowMaze2")).ignoreDirectionDifference()
        });
    }
}
