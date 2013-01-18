/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class NearTen extends BatExercise {
  public NearTen(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("nearTen");
    myWorld.addTest(VISIBLE, 12) ;
    myWorld.addTest(VISIBLE, 17) ;
    myWorld.addTest(VISIBLE, 19) ;
    myWorld.addTest(INVISIBLE, 21) ;
    myWorld.addTest(INVISIBLE, 6) ;
    myWorld.addTest(INVISIBLE, 10) ;
    myWorld.addTest(INVISIBLE, 11) ;
    myWorld.addTest(INVISIBLE, 12) ;
    myWorld.addTest(INVISIBLE, 13) ;
    myWorld.addTest(INVISIBLE, 54) ;
    myWorld.addTest(INVISIBLE, 155) ;
    myWorld.addTest(INVISIBLE, 158) ;
    myWorld.addTest(INVISIBLE, 3) ;
    myWorld.addTest(INVISIBLE, 1) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( nearTen((Integer)t.getParameter(0)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean nearTen(int num) {
  /* BEGIN SOLUTION */
  return (num % 10) <= 2 || (num % 10) >= 8; 
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
