/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class WithoutDoubles extends BatExercise {
  public WithoutDoubles(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("withoutDoubles");
    myWorld.addTest(VISIBLE, 2, 3, true) ;
    myWorld.addTest(VISIBLE, 3, 3, true) ;
    myWorld.addTest(VISIBLE, 3, 3, false) ;
    myWorld.addTest(INVISIBLE, 2, 3, false) ;
    myWorld.addTest(INVISIBLE, 5, 4, true) ;
    myWorld.addTest(INVISIBLE, 5, 4, false) ;
    myWorld.addTest(INVISIBLE, 5, 5, true) ;
    myWorld.addTest(INVISIBLE, 5, 5, false) ;
    myWorld.addTest(INVISIBLE, 6, 6, true) ;
    myWorld.addTest(INVISIBLE, 6, 6, false) ;
    myWorld.addTest(INVISIBLE, 1, 6, true) ;
    myWorld.addTest(INVISIBLE, 6, 1, false) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( withoutDoubles((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Boolean)t.getParameter(2)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int withoutDoubles(int die1, int die2, boolean noDoubles) {
  /* BEGIN SOLUTION */
  if (noDoubles && (die1 == die2)) {
     if (die1 == 6)
        return 1 + die2;
     else 
        return die1 + 1 + die2;
   } else 
		return die1 + die2;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
