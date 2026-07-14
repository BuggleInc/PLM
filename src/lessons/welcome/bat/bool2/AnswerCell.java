/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class AnswerCell extends BatExercise {
  public AnswerCell(Lesson lesson)
  {
    super(lesson);

    BatWorld myWorld = new BatWorld("answerCell");
    myWorld.addTest(VISIBLE, false, false, false);
    myWorld.addTest(VISIBLE, false, false, true);
    myWorld.addTest(VISIBLE, true, false, false);
    myWorld.addTest(INVISIBLE, true, true, false);
    myWorld.addTest(INVISIBLE, false, true, false);
    myWorld.addTest(INVISIBLE, true, true, true);

    setup(myWorld);
  }
}
