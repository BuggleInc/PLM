package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class SumDouble extends BatExercise {

  public SumDouble(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("sumDouble");
    myWorld.addTest(VISIBLE, 1, 2);
    myWorld.addTest(VISIBLE, 3, 2);
    myWorld.addTest(VISIBLE, 2, 2);

    myWorld.addTest(INVISIBLE, -1, 0);
    myWorld.addTest(INVISIBLE, 0, 0);
    myWorld.addTest(INVISIBLE, 0, 1);

    setup(myWorld);
  }
}
