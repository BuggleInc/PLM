/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class AlarmClock extends BatExercise {
  public AlarmClock(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("alarmClock");
    myWorld.addTest(VISIBLE, 1, false);
    myWorld.addTest(VISIBLE, 5, false);
    myWorld.addTest(VISIBLE, 0, false);
    myWorld.addTest(INVISIBLE, 6, false);
    myWorld.addTest(INVISIBLE, 0, true);
    myWorld.addTest(INVISIBLE, 6, true);
    myWorld.addTest(INVISIBLE, 1, true);
    myWorld.addTest(INVISIBLE, 3, true);
    myWorld.addTest(INVISIBLE, 5, true);

    setup(myWorld);
  }
}
