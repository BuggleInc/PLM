/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class DateFashion extends BatExercise {
  public DateFashion(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("dateFashion");
    myWorld.addTest(VISIBLE, 5, 10);
    myWorld.addTest(VISIBLE, 5, 2);
    myWorld.addTest(VISIBLE, 5, 5);
    myWorld.addTest(INVISIBLE, 3, 3);
    myWorld.addTest(INVISIBLE, 10, 2);
    myWorld.addTest(INVISIBLE, 2, 9);
    myWorld.addTest(INVISIBLE, 9, 9);
    myWorld.addTest(INVISIBLE, 10, 5);
    myWorld.addTest(INVISIBLE, 2, 2);
    myWorld.addTest(INVISIBLE, 3, 7);
    myWorld.addTest(INVISIBLE, 2, 7);
    myWorld.addTest(INVISIBLE, 6, 2);

    setup(myWorld);
  }
}
