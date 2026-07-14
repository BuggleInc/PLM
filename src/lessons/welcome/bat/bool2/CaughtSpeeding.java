/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class CaughtSpeeding extends BatExercise {
  public CaughtSpeeding(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("caughtSpeeding");
    myWorld.addTest(VISIBLE, 60, false);
    myWorld.addTest(VISIBLE, 65, false);
    myWorld.addTest(VISIBLE, 65, true);
    myWorld.addTest(INVISIBLE, 80, false);
    myWorld.addTest(INVISIBLE, 85, false);
    myWorld.addTest(INVISIBLE, 85, true);
    myWorld.addTest(INVISIBLE, 70, false);
    myWorld.addTest(INVISIBLE, 75, false);
    myWorld.addTest(INVISIBLE, 75, true);
    myWorld.addTest(INVISIBLE, 40, false);
    myWorld.addTest(INVISIBLE, 40, true);
    myWorld.addTest(INVISIBLE, 90, false);

    setup(myWorld);
  }
}
