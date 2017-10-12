package maze.island;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

import java.io.IOException;

public class IslandMaze extends ExerciseTemplated {

    public IslandMaze() throws IOException, BrokenWorldFileException {
        super("IslandMaze", "IslandMaze");
        tabName = "Escaper";

		/* Create initial situation */
        setup(new World[]{
                ((BuggleWorld) BuggleWorld.newFromFile("maze/island/IslandMaze")).ignoreDirectionDifference(),
                ((BuggleWorld) BuggleWorld.newFromFile("maze/island/IslandMaze2")).ignoreDirectionDifference()
        });
    }
}
