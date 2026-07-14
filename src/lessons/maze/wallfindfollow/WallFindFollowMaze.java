package lessons.maze.wallfindfollow;

import java.io.IOException;
import java.util.Arrays;
import lessons.turmites.helloturmite.HelloTurmiteEntity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class WallFindFollowMaze extends ExerciseTemplated {

  public WallFindFollowMaze(Lesson lesson) throws IOException, BrokenWorldFileException
  {
    super(lesson);
    tabName = "Escaper";

    World[] myWorlds = {((BuggleWorld)BuggleWorld.newFromFile("lessons/maze/wallfindfollow/WallFindFollowMaze")).ignoreDirectionDifference(),
                        ((BuggleWorld)BuggleWorld.newFromFile("lessons/maze/wallfindfollow/WallFindFollowMaze2")).ignoreDirectionDifference()};

    myWorlds = Arrays.stream(myWorlds).map(s -> s.replaceEntities(WallFindFollowMazeEntity::new)).toList().toArray(new World[0]);
    setup(myWorlds);
  }
}
