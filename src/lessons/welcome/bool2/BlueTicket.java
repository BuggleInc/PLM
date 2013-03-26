/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class BlueTicket extends BatExercise {
  public BlueTicket(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("BlueTicket");
    myWorld.addTest(VISIBLE, 9, 1, 0) ;
    myWorld.addTest(VISIBLE, 9, 2, 0) ;
    myWorld.addTest(VISIBLE, 6, 1, 4) ;
    myWorld.addTest(INVISIBLE, 6, 1, 5) ;
    myWorld.addTest(INVISIBLE, 10, 0, 0) ;
    myWorld.addTest(INVISIBLE, 15, 0, 5) ;
    myWorld.addTest(INVISIBLE, 5, 15, 5) ;
    myWorld.addTest(INVISIBLE, 4, 11, 1) ;
    myWorld.addTest(INVISIBLE, 13, 2, 3) ;
    myWorld.addTest(INVISIBLE, 8, 4, 3) ;
    myWorld.addTest(INVISIBLE, 8, 4, 2) ;
    myWorld.addTest(INVISIBLE, 8, 4, 1) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( blueTicket((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int blueTicket(int a, int b, int c) {
  /* BEGIN SOLUTION */
	  int ab = a + b;
	  int ac = a + c;
	  int bc = b + c;
	  
	  if (ab == 10 || ac == 10 || bc == 10)
	     return 10;
	  else if (ab == (bc + 10) || ab == (ac + 10))
	     return 5;
	  else 
	     return 0;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
