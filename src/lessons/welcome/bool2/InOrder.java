/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class InOrder extends BatExercise {
  public InOrder(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("InOrder");
    myWorld.addTest(VISIBLE, 1, 2, 4, false) ;
    myWorld.addTest(VISIBLE, 1, 2, 1, false) ;
    myWorld.addTest(VISIBLE, 1, 1, 2, true) ;
    myWorld.addTest(INVISIBLE, 3, 2, 4, false) ;
    myWorld.addTest(INVISIBLE, 2, 3, 4, false) ;
    myWorld.addTest(INVISIBLE, 3, 2, 4, true) ;
    myWorld.addTest(INVISIBLE, 4, 2, 2, true) ;
    myWorld.addTest(INVISIBLE, 4, 5, 2, true) ;
    myWorld.addTest(INVISIBLE, 2, 4, 6, true) ;
    myWorld.addTest(INVISIBLE, 7, 9, 10, false) ;
    myWorld.addTest(INVISIBLE, 7, 5, 6, true) ;
    myWorld.addTest(INVISIBLE, 7, 5, 4, true) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( inOrder((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2), (Boolean)t.getParameter(3)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean inOrder(int a, int b, int c, boolean bOk) {
  /* BEGIN SOLUTION */
  return (bOk || (b > a)) && (c > b);
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
