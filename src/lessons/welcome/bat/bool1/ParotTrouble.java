package lessons.welcome.bat.bool1;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class ParotTrouble extends BatExercise {

  public ParotTrouble(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("parotTrouble");
    myWorld.addTest(VISIBLE, true, 6);
    myWorld.addTest(VISIBLE, true, 7);
    myWorld.addTest(VISIBLE, false, 6);

    myWorld.addTest(INVISIBLE, true, 21);
    myWorld.addTest(INVISIBLE, false, 21);
    myWorld.addTest(INVISIBLE, true, 23);
    myWorld.addTest(INVISIBLE, false, 23);
    myWorld.addTest(INVISIBLE, true, 20);

    setup(myWorld);
  }
}
