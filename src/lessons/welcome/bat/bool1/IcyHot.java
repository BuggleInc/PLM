package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class IcyHot extends BatExercise {

  public IcyHot(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("icyHot");
    myWorld.addTest(VISIBLE, 120, -1);
    myWorld.addTest(VISIBLE, -1, 120);
    myWorld.addTest(VISIBLE, 2, 120);

    myWorld.addTest(INVISIBLE, -1, 100);
    myWorld.addTest(INVISIBLE, -2, -2);
    myWorld.addTest(INVISIBLE, 120, 120);

    setup(myWorld);
  }
}
