/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class CigarParty extends BatExercise {
  public CigarParty(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("cigarParty");
    myWorld.addTest(VISIBLE, 30, false);
    myWorld.addTest(VISIBLE, 50, false);
    myWorld.addTest(VISIBLE, 70, true);
    myWorld.addTest(INVISIBLE, 30, true);
    myWorld.addTest(INVISIBLE, 50, true);
    myWorld.addTest(INVISIBLE, 60, false);
    myWorld.addTest(INVISIBLE, 61, false);
    myWorld.addTest(INVISIBLE, 40, false);
    myWorld.addTest(INVISIBLE, 39, false);
    myWorld.addTest(INVISIBLE, 40, true);
    myWorld.addTest(INVISIBLE, 39, true);

    setup(myWorld);
  }
}
