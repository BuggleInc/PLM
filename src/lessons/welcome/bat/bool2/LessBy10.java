/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class LessBy10 extends BatExercise {
  public LessBy10(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("lessBy10");
    myWorld.addTest(VISIBLE, 1, 7, 11);
    myWorld.addTest(VISIBLE, 1, 7, 10);
    myWorld.addTest(VISIBLE, 11, 1, 7);
    myWorld.addTest(INVISIBLE, 10, 7, 1);
    myWorld.addTest(INVISIBLE, -10, 2, 2);
    myWorld.addTest(INVISIBLE, 2, 11, 11);
    myWorld.addTest(INVISIBLE, 3, 3, 30);
    myWorld.addTest(INVISIBLE, 3, 3, 3);
    myWorld.addTest(INVISIBLE, 10, 1, 11);
    myWorld.addTest(INVISIBLE, 10, 11, 1);
    myWorld.addTest(INVISIBLE, 10, 11, 2);
    myWorld.addTest(INVISIBLE, 3, 30, 3);
    myWorld.addTest(INVISIBLE, 2, 2, -8);
    myWorld.addTest(INVISIBLE, 2, 8, 12);

    setup(myWorld);
  }
}
