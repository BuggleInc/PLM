package lessons.welcome.methods.flowerpot;

import java.io.IOException;
import java.util.Arrays;
import lessons.turmites.helloturmite.HelloTurmiteEntity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class FlowerCase extends ExerciseTemplated {

  public FlowerCase(Lesson lesson) throws IOException, BrokenWorldFileException
  {
    super(lesson);
    World[] myWorlds = new BuggleWorld[] {(BuggleWorld)BuggleWorld.newFromFile("lessons/welcome/methods/flowerpot/FlowerCase")};

    myWorlds = Arrays.stream(myWorlds).map(s -> s.replaceEntities(FlowerCaseEntity::new)).toList().toArray(new World[0]);
    setup(myWorlds);
  }
}
