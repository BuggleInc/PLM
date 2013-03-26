/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class CigarParty extends BatExercise {
  public CigarParty(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("CigarParty");
    myWorld.addTest(VISIBLE, 30, false) ;
    myWorld.addTest(VISIBLE, 50, false) ;
    myWorld.addTest(VISIBLE, 70, true) ;
    myWorld.addTest(INVISIBLE, 30, true) ;
    myWorld.addTest(INVISIBLE, 50, true) ;
    myWorld.addTest(INVISIBLE, 60, false) ;
    myWorld.addTest(INVISIBLE, 61, false) ;
    myWorld.addTest(INVISIBLE, 40, false) ;
    myWorld.addTest(INVISIBLE, 39, false) ;
    myWorld.addTest(INVISIBLE, 40, true) ;
    myWorld.addTest(INVISIBLE, 39, true) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( cigarParty((Integer)t.getParameter(0), (Boolean)t.getParameter(1)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean cigarParty(int cigars, boolean isWeekend) {
  /* BEGIN SOLUTION */
  return (isWeekend && cigars >= 40) || (!isWeekend && (cigars >= 40) && (cigars <= 60));
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
