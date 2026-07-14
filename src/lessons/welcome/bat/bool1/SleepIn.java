package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class SleepIn extends BatExercise {

  public SleepIn(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("sleepIn");
    myWorld.addTest(VISIBLE, false, false);
    myWorld.addTest(VISIBLE, true, false);
    myWorld.addTest(INVISIBLE, false, true);
    myWorld.addTest(INVISIBLE, true, true);

    setup(myWorld);
  }
}
