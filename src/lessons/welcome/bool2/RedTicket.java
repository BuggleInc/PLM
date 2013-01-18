/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class RedTicket extends BatExercise {
  public RedTicket(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("RedTicket");
    myWorld.addTest(VISIBLE, 2, 2, 2) ;
    myWorld.addTest(VISIBLE, 2, 2, 1) ;
    myWorld.addTest(VISIBLE, 0, 0, 0) ;
    myWorld.addTest(INVISIBLE, 2, 0, 0) ;
    myWorld.addTest(INVISIBLE, 1, 1, 1) ;
    myWorld.addTest(INVISIBLE, 1, 2, 1) ;
    myWorld.addTest(INVISIBLE, 1, 2, 0) ;
    myWorld.addTest(INVISIBLE, 0, 2, 2) ;
    myWorld.addTest(INVISIBLE, 1, 2, 2) ;
    myWorld.addTest(INVISIBLE, 0, 2, 0) ;
    myWorld.addTest(INVISIBLE, 1, 1, 2) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( redTicket((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int redTicket(int a, int b, int c) {
  /* BEGIN SOLUTION */
  if (a == b && b == c && c == 2)
     return 10;
  else if (a == b && b == c)
     return 5;
  else if (b != a && c != a)
	 return 1;
  else
     return 0;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
