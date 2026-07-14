/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class BlueTicket extends BatExercise {
  public BlueTicket(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("blueTicket");
    myWorld.addTest(VISIBLE, 9, 1, 0);
    myWorld.addTest(VISIBLE, 9, 2, 0);
    myWorld.addTest(VISIBLE, 6, 1, 4);
    myWorld.addTest(INVISIBLE, 6, 1, 5);
    myWorld.addTest(INVISIBLE, 10, 0, 0);
    myWorld.addTest(INVISIBLE, 15, 0, 5);
    myWorld.addTest(INVISIBLE, 5, 15, 5);
    myWorld.addTest(INVISIBLE, 4, 11, 1);
    myWorld.addTest(INVISIBLE, 13, 2, 3);
    myWorld.addTest(INVISIBLE, 8, 4, 3);
    myWorld.addTest(INVISIBLE, 8, 4, 2);
    myWorld.addTest(INVISIBLE, 8, 4, 1);

    setup(myWorld);
  }
}
