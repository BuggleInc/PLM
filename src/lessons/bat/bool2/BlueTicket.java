/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.bool2;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class BlueTicket extends BatExercise {
  public BlueTicket(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[12];
    myWorlds[0] = new BatWorld(VISIBLE, 9, 1, 0) ;
    myWorlds[1] = new BatWorld(VISIBLE, 9, 2, 0) ;
    myWorlds[2] = new BatWorld(VISIBLE, 6, 1, 4) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 6, 1, 5) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 10, 0, 0) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 15, 0, 5) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 5, 15, 5) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 4, 11, 1) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 13, 2, 3) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 8, 4, 3) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 8, 4, 2) ;
    myWorlds[11] = new BatWorld(INVISIBLE, 8, 4, 1) ;

    setup(myWorlds,"blueTicket");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = blueTicket((Integer)w.getParameter(0), (Integer)w.getParameter(1), (Integer)w.getParameter(2));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int blueTicket(int a, int b, int c) {
  /* BEGIN SOLUTION */
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
