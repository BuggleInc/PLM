/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.logic1;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class LastDigit extends BatExercise {
  public LastDigit(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[13];
    myWorlds[0] = new BatWorld(VISIBLE, 23, 19, 13) ;
    myWorlds[1] = new BatWorld(VISIBLE, 23, 19, 12) ;
    myWorlds[2] = new BatWorld(VISIBLE, 23, 19, 3) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 23, 19, 39) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 1, 2, 3) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 1, 1, 2) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 1, 2, 2) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 14, 25, 43) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 14, 25, 45) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 248, 106, 1002) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 248, 106, 1008) ;
    myWorlds[11] = new BatWorld(INVISIBLE, 10, 11, 20) ;
    myWorlds[12] = new BatWorld(INVISIBLE, 0, 11, 0) ;

    setup(myWorlds,"lastDigit");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = lastDigit((Integer)w.getParameter(0), (Integer)w.getParameter(1), (Integer)w.getParameter(2));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean lastDigit(int a, int b, int c) {
  /* BEGIN SOLUTION */
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
