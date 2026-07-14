package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class Makes10 extends BatExercise {

  public Makes10(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("makes10");
    myWorld.addTest(VISIBLE, 9, 10);
    myWorld.addTest(VISIBLE, 9, 9);
    myWorld.addTest(VISIBLE, 1, 9);

    myWorld.addTest(INVISIBLE, 10, 1);
    myWorld.addTest(INVISIBLE, 10, 10);
    myWorld.addTest(INVISIBLE, 8, 2);
    myWorld.addTest(INVISIBLE, 8, 3);
    myWorld.addTest(INVISIBLE, 10, 42);
    myWorld.addTest(INVISIBLE, 12, -2);

    setup(myWorld);
  }
}
