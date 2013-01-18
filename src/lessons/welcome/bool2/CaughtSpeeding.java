/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class CaughtSpeeding extends BatExercise {
  public CaughtSpeeding(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("CaughtSpeeding");
    myWorld.addTest(VISIBLE, 60, false) ;
    myWorld.addTest(VISIBLE, 65, false) ;
    myWorld.addTest(VISIBLE, 65, true) ;
    myWorld.addTest(INVISIBLE, 80, false) ;
    myWorld.addTest(INVISIBLE, 85, false) ;
    myWorld.addTest(INVISIBLE, 85, true) ;
    myWorld.addTest(INVISIBLE, 70, false) ;
    myWorld.addTest(INVISIBLE, 75, false) ;
    myWorld.addTest(INVISIBLE, 75, true) ;
    myWorld.addTest(INVISIBLE, 40, false) ;
    myWorld.addTest(INVISIBLE, 40, true) ;
    myWorld.addTest(INVISIBLE, 90, false) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( caughtSpeeding((Integer)t.getParameter(0), (Boolean)t.getParameter(1)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int caughtSpeeding(int speed, boolean isBirthday) {
  /* BEGIN SOLUTION */
  if ((isBirthday && speed <= 65) || (speed <= 60))
     return 0;
  else if ((isBirthday && speed <= 85) || (speed <= 80))
     return 1;
  else 
     return 2;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
