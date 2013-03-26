/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class SquirrelPlay extends BatExercise {
  public SquirrelPlay(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("squirrelPlay");
    myWorld.addTest(VISIBLE, 70, false) ;
    myWorld.addTest(VISIBLE, 95, false) ;
    myWorld.addTest(VISIBLE, 95, true) ;
    myWorld.addTest(INVISIBLE, 90, false) ;
    myWorld.addTest(INVISIBLE, 90, true) ;
    myWorld.addTest(INVISIBLE, 50, false) ;
    myWorld.addTest(INVISIBLE, 50, true) ;
    myWorld.addTest(INVISIBLE, 100, false) ;
    myWorld.addTest(INVISIBLE, 100, true) ;
    myWorld.addTest(INVISIBLE, 105, true) ;
    myWorld.addTest(INVISIBLE, 59, false) ;
    myWorld.addTest(INVISIBLE, 59, true) ;
    myWorld.addTest(INVISIBLE, 60, false) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( squirrelPlay((Integer)t.getParameter(0), (Boolean)t.getParameter(1)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean squirrelPlay(int temp, boolean isSummer) {
  /* BEGIN SOLUTION */
  return (temp >= 60 && ((isSummer && temp <= 100) || temp <= 90));
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
