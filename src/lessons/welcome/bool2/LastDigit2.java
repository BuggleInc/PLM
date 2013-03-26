/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class LastDigit2 extends BatExercise {
  public LastDigit2(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("lastDigit");
    myWorld.addTest(VISIBLE, 23, 19, 13) ;
    myWorld.addTest(VISIBLE, 23, 19, 12) ;
    myWorld.addTest(VISIBLE, 23, 19, 3) ;
    myWorld.addTest(INVISIBLE, 23, 19, 39) ;
    myWorld.addTest(INVISIBLE, 1, 2, 3) ;
    myWorld.addTest(INVISIBLE, 1, 1, 2) ;
    myWorld.addTest(INVISIBLE, 1, 2, 2) ;
    myWorld.addTest(INVISIBLE, 14, 25, 43) ;
    myWorld.addTest(INVISIBLE, 14, 25, 45) ;
    myWorld.addTest(INVISIBLE, 248, 106, 1002) ;
    myWorld.addTest(INVISIBLE, 248, 106, 1008) ;
    myWorld.addTest(INVISIBLE, 10, 11, 20) ;
    myWorld.addTest(INVISIBLE, 0, 11, 0) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( lastDigit((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean lastDigit(int a, int b, int c) {
  /* BEGIN SOLUTION */
	  int da = a % 10;
	  int db = b % 10;
	  int dc = c % 10;
	  return da == db || da == dc || dc == db;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
