package maze.pledge;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

import java.io.IOException;

public class PledgeMaze extends ExerciseTemplated {

    public PledgeMaze(FileUtils fileUtils) throws IOException, BrokenWorldFileException {
        super("PledgeMaze");
        tabName = "Escaper";

        setup(new World[]{
                ((BuggleWorld) BuggleWorld.newFromFile(fileUtils, "maze/pledge/PledgeMaze")).ignoreDirectionDifference(),
                ((BuggleWorld) BuggleWorld.newFromFile(fileUtils, "maze/pledge/PledgeMaze2")).ignoreDirectionDifference()
        });
    }
}
