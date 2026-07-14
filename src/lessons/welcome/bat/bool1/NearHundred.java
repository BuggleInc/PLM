package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class NearHundred extends BatExercise {

  public NearHundred(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("nearHundred");
    myWorld.addTest(VISIBLE, 93);
    myWorld.addTest(VISIBLE, 90);
    myWorld.addTest(VISIBLE, 89);

    myWorld.addTest(INVISIBLE, 110);
    myWorld.addTest(INVISIBLE, 191);
    myWorld.addTest(INVISIBLE, 189);
    myWorld.addTest(INVISIBLE, 200);
    myWorld.addTest(INVISIBLE, 210);
    myWorld.addTest(INVISIBLE, 211);
    myWorld.addTest(INVISIBLE, -100);

    setup(myWorld);
  }
}
