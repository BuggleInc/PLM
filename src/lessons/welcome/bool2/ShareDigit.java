/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class ShareDigit extends BatExercise {
  public ShareDigit(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("shareDigit");
    myWorld.addTest(VISIBLE, 12, 23) ;
    myWorld.addTest(VISIBLE, 12, 43) ;
    myWorld.addTest(VISIBLE, 12, 44) ;
    myWorld.addTest(INVISIBLE, 23, 12) ;
    myWorld.addTest(INVISIBLE, 23, 39) ;
    myWorld.addTest(INVISIBLE, 23, 19) ;
    myWorld.addTest(INVISIBLE, 30, 90) ;
    myWorld.addTest(INVISIBLE, 30, 91) ;
    myWorld.addTest(INVISIBLE, 55, 55) ;
    myWorld.addTest(INVISIBLE, 55, 44) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( shareDigit((Integer)t.getParameter(0), (Integer)t.getParameter(1)) ); 
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean shareDigit(int a, int b) {
  /* BEGIN SOLUTION */
  return (a/10 == b/10 || a/10 == b%10 || a%10 == b/10 || a%10 == b%10);
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
