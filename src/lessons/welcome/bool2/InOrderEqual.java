/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class InOrderEqual extends BatExercise {
  public InOrderEqual(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("inOrderEqual");
    myWorld.addTest(VISIBLE, 2, 5, 11, false) ;
    myWorld.addTest(VISIBLE, 5, 7, 6, false) ;
    myWorld.addTest(VISIBLE, 5, 5, 7, true) ;
    myWorld.addTest(INVISIBLE, 5, 5, 7, false) ;
    myWorld.addTest(INVISIBLE, 2, 5, 4, false) ;
    myWorld.addTest(INVISIBLE, 3, 4, 3, false) ;
    myWorld.addTest(INVISIBLE, 3, 4, 4, false) ;
    myWorld.addTest(INVISIBLE, 3, 4, 3, true) ;
    myWorld.addTest(INVISIBLE, 3, 4, 4, true) ;
    myWorld.addTest(INVISIBLE, 1, 5, 5, true) ;
    myWorld.addTest(INVISIBLE, 5, 5, 5, true) ;
    myWorld.addTest(INVISIBLE, 2, 2, 1, true) ;
    myWorld.addTest(INVISIBLE, 9, 2, 2, true) ;
    myWorld.addTest(INVISIBLE, 0, 1, 0, true) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( inOrderEqual((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2), (Boolean)t.getParameter(3)) ); 
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean inOrderEqual(int a, int b, int c, boolean equalOk) {
  /* BEGIN SOLUTION */
  return (equalOk && ((a <= b) && (b <= c))) || (a < b && b < c);
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
