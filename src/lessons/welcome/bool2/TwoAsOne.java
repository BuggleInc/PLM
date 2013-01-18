/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class TwoAsOne extends BatExercise {
  public TwoAsOne(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("twoAsOne");
    myWorld.addTest(VISIBLE, 1, 2, 3) ;
    myWorld.addTest(VISIBLE, 3, 1, 2) ;
    myWorld.addTest(VISIBLE, 3, 2, 2) ;
    myWorld.addTest(INVISIBLE, 2, 3, 1) ;
    myWorld.addTest(INVISIBLE, 5, 3, -2) ;
    myWorld.addTest(INVISIBLE, 5, 3, -3) ;
    myWorld.addTest(INVISIBLE, 2, 5, 3) ;
    myWorld.addTest(INVISIBLE, 9, 5, 5) ;
    myWorld.addTest(INVISIBLE, 9, 4, 5) ;
    myWorld.addTest(INVISIBLE, 5, 4, 9) ;
    myWorld.addTest(INVISIBLE, 3, 3, 0) ;
    myWorld.addTest(INVISIBLE, 3, 3, 2) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( twoAsOne((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean twoAsOne(int a, int b, int c) {
  /* BEGIN SOLUTION */
  return (a + b == c) || (a + c == b) || (b + c == a);
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
