/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class FizzBuzz extends BatExercise {
  public FizzBuzz(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("fizzBuzz");
    myWorld.addTest(VISIBLE, 2);
    myWorld.addTest(VISIBLE, 3);
    myWorld.addTest(VISIBLE, 4);
    myWorld.addTest(VISIBLE, 5);
    myWorld.addTest(VISIBLE, 6);
    myWorld.addTest(VISIBLE, 7);
    myWorld.addTest(VISIBLE, 8);
    myWorld.addTest(VISIBLE, 9);
    myWorld.addTest(INVISIBLE, 10);
    myWorld.addTest(INVISIBLE, 11);
    myWorld.addTest(INVISIBLE, 12);
    myWorld.addTest(INVISIBLE, 15);
    myWorld.addTest(INVISIBLE, 16);
    myWorld.addTest(INVISIBLE, 18);

    setup(myWorld);
  }
}
