package maze.randommouse;

import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

import java.io.IOException;

public class RandomMouseMaze extends ExerciseTemplated {

    public RandomMouseMaze(FileUtils fileUtils) throws IOException, BrokenWorldFileException {
        super("RandomMouseMaze");
        tabName = "RandomMouseMaze";

        setup(new World[]{
                ((BuggleWorld) BuggleWorld.newFromFile(fileUtils, "maze/randommouse/RandomMouseMaze")).ignoreDirectionDifference(),
                ((BuggleWorld) BuggleWorld.newFromFile(fileUtils, "maze/randommouse/RandomMouseMaze2")).ignoreDirectionDifference()
        });
    }
}
