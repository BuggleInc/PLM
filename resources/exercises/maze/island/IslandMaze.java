package maze.island;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

import java.io.IOException;

public class IslandMaze extends ExerciseTemplated {

    public IslandMaze(FileUtils fileUtils) throws IOException, BrokenWorldFileException {
        super("IslandMaze");
        tabName = "Escaper";

		/* Create initial situation */
        setup(new World[]{
                ((BuggleWorld) BuggleWorld.newFromFile(fileUtils, "maze/island/IslandMaze")).ignoreDirectionDifference(),
                ((BuggleWorld) BuggleWorld.newFromFile(fileUtils, "maze/island/IslandMaze2")).ignoreDirectionDifference()
        });
    }
}
