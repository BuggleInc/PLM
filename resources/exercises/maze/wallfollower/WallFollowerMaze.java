package maze.wallfollower;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

import java.io.IOException;

public class WallFollowerMaze extends ExerciseTemplated {

    public WallFollowerMaze() throws IOException, BrokenWorldFileException {
        super("WallFollowerMaze", "WallFollowerMaze");
        tabName = "Escaper";

        setup(new World[]{
                ((BuggleWorld) BuggleWorld.newFromFile("maze/wallfollower/WallFollowerMaze")).ignoreDirectionDifference(),
                ((BuggleWorld) BuggleWorld.newFromFile("maze/wallfollower/WallFollowerMaze2")).ignoreDirectionDifference()
        });
    }
}
