package lessons.maze.shortestpath;

import java.io.IOException;
import java.util.Arrays;
import lessons.turmites.helloturmite.HelloTurmiteEntity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class ShortestPathMaze extends ExerciseTemplated {

  public ShortestPathMaze(Lesson lesson) throws IOException, BrokenWorldFileException
  {
    super(lesson);
    tabName = "JediEscaper";

    World[] myWorlds = {((BuggleWorld)BuggleWorld.newFromFile("lessons/maze/shortestpath/WallFollowerMaze")).ignoreDirectionDifference(),
                        ((BuggleWorld)BuggleWorld.newFromFile("lessons/maze/shortestpath/PledgeMaze")).ignoreDirectionDifference()};

    myWorlds = Arrays.stream(myWorlds).map(s -> s.replaceEntities(ShortestPathMazeEntity::new)).toList().toArray(new World[0]);
    setup(myWorlds);
  }
}
