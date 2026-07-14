/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class GreenTicket extends BatExercise {
  public GreenTicket(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("greenTicket");
    myWorld.addTest(VISIBLE, 1, 2, 3);
    myWorld.addTest(VISIBLE, 2, 2, 2);
    myWorld.addTest(VISIBLE, 1, 1, 2);
    myWorld.addTest(INVISIBLE, 2, 1, 1);
    myWorld.addTest(INVISIBLE, 1, 2, 1);
    myWorld.addTest(INVISIBLE, 3, 2, 1);
    myWorld.addTest(INVISIBLE, 0, 0, 0);
    myWorld.addTest(INVISIBLE, 2, 0, 0);
    myWorld.addTest(INVISIBLE, 0, 9, 10);
    myWorld.addTest(INVISIBLE, 0, 10, 0);
    myWorld.addTest(INVISIBLE, 9, 9, 9);
    myWorld.addTest(INVISIBLE, 9, 0, 9);

    setup(myWorld);
  }
}
