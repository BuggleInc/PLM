/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class In1To10 extends BatExercise {
  public In1To10(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("in1To10");
    myWorld.addTest(VISIBLE, 5, false);
    myWorld.addTest(VISIBLE, 11, false);
    myWorld.addTest(VISIBLE, 11, true);
    myWorld.addTest(INVISIBLE, 10, false);
    myWorld.addTest(INVISIBLE, 10, true);
    myWorld.addTest(INVISIBLE, 9, false);
    myWorld.addTest(INVISIBLE, 9, true);
    myWorld.addTest(INVISIBLE, 1, false);
    myWorld.addTest(INVISIBLE, 1, true);
    myWorld.addTest(INVISIBLE, 0, false);
    myWorld.addTest(INVISIBLE, 0, true);
    myWorld.addTest(INVISIBLE, -1, false);

    setup(myWorld);
  }
}
