/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.bool2;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class MaxMod5 extends BatExercise {
  public MaxMod5(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[11];
    myWorlds[0] = new BatWorld(VISIBLE, 2, 3) ;
    myWorlds[1] = new BatWorld(VISIBLE, 6, 2) ;
    myWorlds[2] = new BatWorld(VISIBLE, 3, 2) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 8, 12) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 7, 12) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 11, 6) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 2, 7) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 7, 7) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 9, 1) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 9, 14) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 1, 2) ;

    setup(myWorlds,"maxMod5");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = maxMod5((Integer)w.getParameter(0), (Integer)w.getParameter(1));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int maxMod5(int a, int b) {
  /* BEGIN SOLUTION */
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
