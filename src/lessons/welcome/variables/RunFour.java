package lessons.welcome.variables;

import java.io.IOException;
import java.util.Arrays;
import lessons.turmites.helloturmite.HelloTurmiteEntity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class RunFour extends ExerciseTemplated {

  public RunFour(Lesson lesson) throws IOException, BrokenWorldFileException
  {
    super(lesson);

    /* Create initial situation */
    World[] myWorlds = new World[] {
        BuggleWorld.newFromFile("lessons/welcome/variables/RunFour"),
    };
    for (World w : myWorlds)
      w.setDelay(50); // moving a bit faster than usual

    myWorlds = Arrays.stream(myWorlds).map(s -> s.replaceEntities(RunFourEntity::new)).toList().toArray(new World[0]);
    setup(myWorlds);
  }
}