package lessons.welcome.bool1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class LastDigit extends BatExercise {
  public LastDigit(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("LastDigit");
    myWorld.addTest(VISIBLE, 7, 17) ;
    myWorld.addTest(VISIBLE, 6, 17) ;
    myWorld.addTest(VISIBLE, 3, 113) ;
    myWorld.addTest(INVISIBLE, 114, 113) ;
    myWorld.addTest(INVISIBLE, 114, 4) ;
    myWorld.addTest(INVISIBLE, 10, 0) ;
    myWorld.addTest(INVISIBLE, 11, 0) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( lastDigit((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean lastDigit(int a, int b) {
  /* BEGIN SOLUTION */
	  return a%10 == b%10;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
