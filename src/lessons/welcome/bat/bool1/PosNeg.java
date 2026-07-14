package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class PosNeg extends BatExercise {

  public PosNeg(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("posNeg");
    myWorld.addTest(VISIBLE, -1, 1, false);
    myWorld.addTest(VISIBLE, 1, -1, false);
    myWorld.addTest(VISIBLE, 1, 1, false);

    myWorld.addTest(INVISIBLE, -1, -1, false);
    myWorld.addTest(INVISIBLE, 1, -1, true);
    myWorld.addTest(INVISIBLE, -1, 1, true);
    myWorld.addTest(INVISIBLE, 1, 1, true);
    myWorld.addTest(INVISIBLE, -1, -1, true);
    myWorld.addTest(INVISIBLE, 5, -5, true);
    myWorld.addTest(INVISIBLE, -6, 6, false);
    myWorld.addTest(INVISIBLE, -5, -5, false);
    myWorld.addTest(INVISIBLE, -5, 5, true);
    myWorld.addTest(INVISIBLE, -5, -5, true);

    setup(myWorld);
  }
}
