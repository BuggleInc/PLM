/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.logic1;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class InOrder extends BatExercise {
  public InOrder(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[12];
    myWorlds[0] = new BatWorld(VISIBLE, 1, 2, 4, false) ;
    myWorlds[1] = new BatWorld(VISIBLE, 1, 2, 1, false) ;
    myWorlds[2] = new BatWorld(VISIBLE, 1, 1, 2, true) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 3, 2, 4, false) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 2, 3, 4, false) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 3, 2, 4, true) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 4, 2, 2, true) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 4, 5, 2, true) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 2, 4, 6, true) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 7, 9, 10, false) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 7, 5, 6, true) ;
    myWorlds[11] = new BatWorld(INVISIBLE, 7, 5, 4, true) ;

    setup(myWorlds,"inOrder");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = inOrder((Integer)w.getParameter(0), (Integer)w.getParameter(1), (Integer)w.getParameter(2), (Boolean)w.getParameter(3));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean inOrder(int a, int b, int c, boolean bOk) {
  /* BEGIN SOLUTION */
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
