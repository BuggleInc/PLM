/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class WithoutDoubles extends BatExercise {
  public WithoutDoubles(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("withoutDoubles");
    myWorld.addTest(VISIBLE, 2, 3, true);
    myWorld.addTest(VISIBLE, 3, 3, true);
    myWorld.addTest(VISIBLE, 3, 3, false);
    myWorld.addTest(INVISIBLE, 2, 3, false);
    myWorld.addTest(INVISIBLE, 5, 4, true);
    myWorld.addTest(INVISIBLE, 5, 4, false);
    myWorld.addTest(INVISIBLE, 5, 5, true);
    myWorld.addTest(INVISIBLE, 5, 5, false);
    myWorld.addTest(INVISIBLE, 6, 6, true);
    myWorld.addTest(INVISIBLE, 6, 6, false);
    myWorld.addTest(INVISIBLE, 1, 6, true);
    myWorld.addTest(INVISIBLE, 6, 1, false);

    setup(myWorld);
  }
}
