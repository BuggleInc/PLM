/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class MaxMod5 extends BatExercise {
  public MaxMod5(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("maxMod5");
    myWorld.addTest(VISIBLE, 2, 3) ;
    myWorld.addTest(VISIBLE, 6, 2) ;
    myWorld.addTest(VISIBLE, 3, 2) ;
    myWorld.addTest(INVISIBLE, 8, 12) ;
    myWorld.addTest(INVISIBLE, 7, 12) ;
    myWorld.addTest(INVISIBLE, 11, 6) ;
    myWorld.addTest(INVISIBLE, 2, 7) ;
    myWorld.addTest(INVISIBLE, 7, 7) ;
    myWorld.addTest(INVISIBLE, 9, 1) ;
    myWorld.addTest(INVISIBLE, 9, 14) ;
    myWorld.addTest(INVISIBLE, 1, 2) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( maxMod5((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int maxMod5(int a, int b) {
  /* BEGIN SOLUTION */
	   if (a == b)
		      return 0;
		   else if (a > b)
		           if (a % 5 == b % 5)
		              return b;
		           else 
		              return a;
		        else 
		           if (a % 5 == b % 5)
		              return a;
		           else 
		              return b;  
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
