/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class In1To10 extends BatExercise {
  public In1To10(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("In1To10");
    myWorld.addTest(VISIBLE, 5, false) ;
    myWorld.addTest(VISIBLE, 11, false) ;
    myWorld.addTest(VISIBLE, 11, true) ;
    myWorld.addTest(INVISIBLE, 10, false) ;
    myWorld.addTest(INVISIBLE, 10, true) ;
    myWorld.addTest(INVISIBLE, 9, false) ;
    myWorld.addTest(INVISIBLE, 9, true) ;
    myWorld.addTest(INVISIBLE, 1, false) ;
    myWorld.addTest(INVISIBLE, 1, true) ;
    myWorld.addTest(INVISIBLE, 0, false) ;
    myWorld.addTest(INVISIBLE, 0, true) ;
    myWorld.addTest(INVISIBLE, -1, false) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( in1To10((Integer)t.getParameter(0), (Boolean)t.getParameter(1)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean in1To10(int n, boolean outsideMode) {
  /* BEGIN SOLUTION */
  return (outsideMode && (n <= 1 || n >= 10)) || ((! outsideMode) && (n >= 1 && n <= 10));
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
