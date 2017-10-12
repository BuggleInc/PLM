package maze.shortestpath;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

import java.io.IOException;

public class ShortestPathMaze extends ExerciseTemplated {

    public ShortestPathMaze() throws IOException, BrokenWorldFileException {
        super("ShortestPathMaze", "ShortestPathMaze");
        tabName = "JediEscaper";

        setup(new World[]{
                ((BuggleWorld) BuggleWorld.newFromFile("maze/shortestpath/WallFollowerMaze")).ignoreDirectionDifference(),
                ((BuggleWorld) BuggleWorld.newFromFile("maze/shortestpath/PledgeMaze")).ignoreDirectionDifference()
        });
    }
}
