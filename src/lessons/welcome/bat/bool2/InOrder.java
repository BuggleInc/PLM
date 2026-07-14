/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class InOrder extends BatExercise {
  public InOrder(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("inOrder");
    myWorld.addTest(VISIBLE, 1, 2, 4, false);
    myWorld.addTest(VISIBLE, 1, 2, 1, false);
    myWorld.addTest(VISIBLE, 1, 1, 2, true);
    myWorld.addTest(INVISIBLE, 3, 2, 4, false);
    myWorld.addTest(INVISIBLE, 2, 3, 4, false);
    myWorld.addTest(INVISIBLE, 3, 2, 4, true);
    myWorld.addTest(INVISIBLE, 4, 2, 2, true);
    myWorld.addTest(INVISIBLE, 4, 5, 2, true);
    myWorld.addTest(INVISIBLE, 2, 4, 6, true);
    myWorld.addTest(INVISIBLE, 7, 9, 10, false);
    myWorld.addTest(INVISIBLE, 7, 5, 6, true);
    myWorld.addTest(INVISIBLE, 7, 5, 4, true);

    setup(myWorld);
  }
}
