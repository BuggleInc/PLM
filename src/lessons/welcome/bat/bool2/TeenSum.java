/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class TeenSum extends BatExercise {
  public TeenSum(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("teenSum");
    myWorld.addTest(VISIBLE, 3, 4);
    myWorld.addTest(VISIBLE, 10, 13);
    myWorld.addTest(VISIBLE, 13, 2);
    myWorld.addTest(INVISIBLE, 3, 19);
    myWorld.addTest(INVISIBLE, 13, 13);
    myWorld.addTest(INVISIBLE, 10, 10);
    myWorld.addTest(INVISIBLE, 6, 14);
    myWorld.addTest(INVISIBLE, 15, 2);
    myWorld.addTest(INVISIBLE, 19, 19);
    myWorld.addTest(INVISIBLE, 19, 20);
    myWorld.addTest(INVISIBLE, 2, 18);
    myWorld.addTest(INVISIBLE, 12, 4);
    myWorld.addTest(INVISIBLE, 2, 20);
    myWorld.addTest(INVISIBLE, 2, 17);
    myWorld.addTest(INVISIBLE, 2, 16);
    myWorld.addTest(INVISIBLE, 6, 7);

    setup(myWorld);
  }
}
