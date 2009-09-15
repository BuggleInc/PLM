/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.logic1;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class AnswerCell extends BatExercise {
  public AnswerCell(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[6];
    myWorlds[0] = new BatWorld(VISIBLE, false, false, false) ;
    myWorlds[1] = new BatWorld(VISIBLE, false, false, true) ;
    myWorlds[2] = new BatWorld(VISIBLE, true, false, false) ;
    myWorlds[3] = new BatWorld(INVISIBLE, true, true, false) ;
    myWorlds[4] = new BatWorld(INVISIBLE, false, true, false) ;
    myWorlds[5] = new BatWorld(INVISIBLE, true, true, true) ;

    setup(myWorlds,"answerCell");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = answerCell((Boolean)w.getParameter(0), (Boolean)w.getParameter(1), (Boolean)w.getParameter(2));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean answerCell(boolean isMorning, boolean isMom, boolean isAsleep) {
  /* BEGIN SOLUTION */
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
