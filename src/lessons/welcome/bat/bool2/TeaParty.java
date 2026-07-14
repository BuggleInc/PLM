/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class TeaParty extends BatExercise {
  public TeaParty(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("teaParty");
    myWorld.addTest(VISIBLE, 6, 8);
    myWorld.addTest(VISIBLE, 3, 8);
    myWorld.addTest(VISIBLE, 20, 6);
    myWorld.addTest(INVISIBLE, 12, 6);
    myWorld.addTest(INVISIBLE, 11, 6);
    myWorld.addTest(INVISIBLE, 11, 4);
    myWorld.addTest(INVISIBLE, 4, 5);
    myWorld.addTest(INVISIBLE, 5, 5);
    myWorld.addTest(INVISIBLE, 6, 6);
    myWorld.addTest(INVISIBLE, 5, 10);
    myWorld.addTest(INVISIBLE, 5, 9);
    myWorld.addTest(INVISIBLE, 10, 4);
    myWorld.addTest(INVISIBLE, 10, 20);

    setup(myWorld);
  }
}
