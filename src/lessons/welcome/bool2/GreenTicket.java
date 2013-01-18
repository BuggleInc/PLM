/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class GreenTicket extends BatExercise {
  public GreenTicket(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("GreenTicket");
    myWorld.addTest(VISIBLE, 1, 2, 3) ;
    myWorld.addTest(VISIBLE, 2, 2, 2) ;
    myWorld.addTest(VISIBLE, 1, 1, 2) ;
    myWorld.addTest(INVISIBLE, 2, 1, 1) ;
    myWorld.addTest(INVISIBLE, 1, 2, 1) ;
    myWorld.addTest(INVISIBLE, 3, 2, 1) ;
    myWorld.addTest(INVISIBLE, 0, 0, 0) ;
    myWorld.addTest(INVISIBLE, 2, 0, 0) ;
    myWorld.addTest(INVISIBLE, 0, 9, 10) ;
    myWorld.addTest(INVISIBLE, 0, 10, 0) ;
    myWorld.addTest(INVISIBLE, 9, 9, 9) ;
    myWorld.addTest(INVISIBLE, 9, 0, 9) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( greenTicket((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int greenTicket(int a, int b, int c) {
  /* BEGIN SOLUTION */
  if (a == b && b == c)
     return 20;
  else if (a == b || b == c || a == c)
     return 10;
  else  // (a != b && b != a && c != a)
     return 0;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
