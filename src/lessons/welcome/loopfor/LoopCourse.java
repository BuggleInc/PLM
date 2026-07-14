package lessons.welcome.loopfor;

import java.io.IOException;
import java.util.Arrays;
import lessons.turmites.helloturmite.HelloTurmiteEntity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class LoopCourse extends ExerciseTemplated {

  public LoopCourse(Lesson lesson) throws IOException, BrokenWorldFileException
  {
    super(lesson);
    tabName = "Runner";

    /* Create initial situation */
    World[] myWorlds = new World[] {BuggleWorld.newFromFile("lessons/welcome/loopfor/LoopCourse")};
    for (World w : myWorlds)
      w.setDelay(10); // runners are moving faster than usual

    myWorlds = Arrays.stream(myWorlds).map(s -> s.replaceEntities(LoopCourseEntity::new)).toList().toArray(new World[0]);
    setup(myWorlds);
  }
}
