/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class TeaParty extends BatExercise {
  public TeaParty(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("teaParty");
    myWorld.addTest(VISIBLE, 6, 8) ;
    myWorld.addTest(VISIBLE, 3, 8) ;
    myWorld.addTest(VISIBLE, 20, 6) ;
    myWorld.addTest(INVISIBLE, 12, 6) ;
    myWorld.addTest(INVISIBLE, 11, 6) ;
    myWorld.addTest(INVISIBLE, 11, 4) ;
    myWorld.addTest(INVISIBLE, 4, 5) ;
    myWorld.addTest(INVISIBLE, 5, 5) ;
    myWorld.addTest(INVISIBLE, 6, 6) ;
    myWorld.addTest(INVISIBLE, 5, 10) ;
    myWorld.addTest(INVISIBLE, 5, 9) ;
    myWorld.addTest(INVISIBLE, 10, 4) ;
    myWorld.addTest(INVISIBLE, 10, 20) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( teaParty((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int teaParty(int tea, int candy) {
  /* BEGIN SOLUTION */
  if (tea < 5 || candy < 5)
     return 0;
  else if (tea >= 2*candy || candy >= 2*tea) 
     return 2;
  else // (tea >= 5 && candy >= 5)
     return 1;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
