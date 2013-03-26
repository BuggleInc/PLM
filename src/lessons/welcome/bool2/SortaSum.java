/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class SortaSum extends BatExercise {
  public SortaSum(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("sortaSum");
    myWorld.addTest(VISIBLE, 3, 4) ;
    myWorld.addTest(VISIBLE, 9, 4) ;
    myWorld.addTest(VISIBLE, 10, 11) ;
    myWorld.addTest(INVISIBLE, 12, -3) ;
    myWorld.addTest(INVISIBLE, -3, 12) ;
    myWorld.addTest(INVISIBLE, 4, 5) ;
    myWorld.addTest(INVISIBLE, 4, 6) ;
    myWorld.addTest(INVISIBLE, 14, 7) ;
    myWorld.addTest(INVISIBLE, 14, 6) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( sortaSum((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int sortaSum(int a, int b) {
  /* BEGIN SOLUTION */
    int sum = a+b;
    if (sum >= 10 && sum <= 19)
	   return 20;
    else
	   return sum;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
