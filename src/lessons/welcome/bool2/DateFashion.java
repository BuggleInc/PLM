/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class DateFashion extends BatExercise {
  public DateFashion(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("DateFashion");
    myWorld.addTest(VISIBLE, 5, 10) ;
    myWorld.addTest(VISIBLE, 5, 2) ;
    myWorld.addTest(VISIBLE, 5, 5) ;
    myWorld.addTest(INVISIBLE, 3, 3) ;
    myWorld.addTest(INVISIBLE, 10, 2) ;
    myWorld.addTest(INVISIBLE, 2, 9) ;
    myWorld.addTest(INVISIBLE, 9, 9) ;
    myWorld.addTest(INVISIBLE, 10, 5) ;
    myWorld.addTest(INVISIBLE, 2, 2) ;
    myWorld.addTest(INVISIBLE, 3, 7) ;
    myWorld.addTest(INVISIBLE, 2, 7) ;
    myWorld.addTest(INVISIBLE, 6, 2) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( dateFashion((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int dateFashion(int you, int date) {
  /* BEGIN SOLUTION */
    if (you <= 2 || date <= 2)
       return 0;
    else if (you >= 8 || date >= 8)
	   return 2;
    else
	   return 1;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
