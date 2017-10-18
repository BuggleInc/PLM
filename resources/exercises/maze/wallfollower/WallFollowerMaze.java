package maze.wallfollower;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

import java.io.IOException;

public class WallFollowerMaze extends ExerciseTemplated {

    public WallFollowerMaze(FileUtils fileUtils) throws IOException, BrokenWorldFileException {
        super("WallFollowerMaze");
        tabName = "Escaper";

        setup(new World[]{
                ((BuggleWorld) BuggleWorld.newFromFile(fileUtils, "maze/wallfollower/WallFollowerMaze")).ignoreDirectionDifference(),
                ((BuggleWorld) BuggleWorld.newFromFile(fileUtils, "maze/wallfollower/WallFollowerMaze2")).ignoreDirectionDifference()
        });
    }
}
