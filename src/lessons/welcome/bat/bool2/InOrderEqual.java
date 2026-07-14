/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class InOrderEqual extends BatExercise {
  public InOrderEqual(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("inOrderEqual");
    myWorld.addTest(VISIBLE, 2, 5, 11, false);
    myWorld.addTest(VISIBLE, 5, 7, 6, false);
    myWorld.addTest(VISIBLE, 5, 5, 7, true);
    myWorld.addTest(INVISIBLE, 5, 5, 7, false);
    myWorld.addTest(INVISIBLE, 2, 5, 4, false);
    myWorld.addTest(INVISIBLE, 3, 4, 3, false);
    myWorld.addTest(INVISIBLE, 3, 4, 4, false);
    myWorld.addTest(INVISIBLE, 3, 4, 3, true);
    myWorld.addTest(INVISIBLE, 3, 4, 4, true);
    myWorld.addTest(INVISIBLE, 1, 5, 5, true);
    myWorld.addTest(INVISIBLE, 5, 5, 5, true);
    myWorld.addTest(INVISIBLE, 2, 2, 1, true);
    myWorld.addTest(INVISIBLE, 9, 2, 2, true);
    myWorld.addTest(INVISIBLE, 0, 1, 0, true);

    setup(myWorld);
  }
}
