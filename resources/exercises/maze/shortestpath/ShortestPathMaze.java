package maze.shortestpath;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

import java.io.IOException;

public class ShortestPathMaze extends ExerciseTemplated {

    public ShortestPathMaze(FileUtils fileUtils) throws IOException, BrokenWorldFileException {
        super("ShortestPathMaze");
        tabName = "JediEscaper";

        setup(new World[]{
                ((BuggleWorld) BuggleWorld.newFromFile(fileUtils, "maze/shortestpath/WallFollowerMaze")).ignoreDirectionDifference(),
                ((BuggleWorld) BuggleWorld.newFromFile(fileUtils, "maze/shortestpath/PledgeMaze")).ignoreDirectionDifference()
        });
    }
}
