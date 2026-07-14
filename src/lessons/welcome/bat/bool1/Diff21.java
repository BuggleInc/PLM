package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class Diff21 extends BatExercise {

  public Diff21(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("diff21");
    myWorld.addTest(VISIBLE, 2);
    myWorld.addTest(VISIBLE, 11);
    myWorld.addTest(VISIBLE, 0);

    myWorld.addTest(INVISIBLE, 19);
    myWorld.addTest(INVISIBLE, 10);
    myWorld.addTest(INVISIBLE, 21);
    myWorld.addTest(INVISIBLE, 22);
    myWorld.addTest(INVISIBLE, 25);
    myWorld.addTest(INVISIBLE, 30);
    myWorld.addTest(INVISIBLE, -21);

    setup(myWorld);
  }
}
