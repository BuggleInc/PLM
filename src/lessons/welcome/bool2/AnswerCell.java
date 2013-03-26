/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class AnswerCell extends BatExercise {
  public AnswerCell(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("AnswerCell");
    myWorld.addTest(VISIBLE, false, false, false) ;
    myWorld.addTest(VISIBLE, false, false, true) ;
    myWorld.addTest(VISIBLE, true, false, false) ;
    myWorld.addTest(INVISIBLE, true, true, false) ;
    myWorld.addTest(INVISIBLE, false, true, false) ;
    myWorld.addTest(INVISIBLE, true, true, true) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( answerCell((Boolean)t.getParameter(0), (Boolean)t.getParameter(1), (Boolean)t.getParameter(2)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean answerCell(boolean isMorning, boolean isMom, boolean isAsleep) {
  /* BEGIN SOLUTION */
  return (! isAsleep) && ! (isMorning && ! isMom);
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
