package maze.pledge;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

import java.io.IOException;

public class PledgeMaze extends ExerciseTemplated {

    public PledgeMaze() throws IOException, BrokenWorldFileException {
        super("PledgeMaze", "PledgeMaze");
        tabName = "Escaper";

        setup(new World[]{
                ((BuggleWorld) BuggleWorld.newFromFile("maze/pledge/PledgeMaze")).ignoreDirectionDifference(),
                ((BuggleWorld) BuggleWorld.newFromFile("maze/pledge/PledgeMaze2")).ignoreDirectionDifference()
        });
    }
}
