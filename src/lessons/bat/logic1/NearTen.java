/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.logic1;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class NearTen extends BatExercise {
  public NearTen(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[14];
    myWorlds[0] = new BatWorld(VISIBLE, 12) ;
    myWorlds[1] = new BatWorld(VISIBLE, 17) ;
    myWorlds[2] = new BatWorld(VISIBLE, 19) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 21) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 6) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 10) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 11) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 12) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 13) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 54) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 155) ;
    myWorlds[11] = new BatWorld(INVISIBLE, 158) ;
    myWorlds[12] = new BatWorld(INVISIBLE, 3) ;
    myWorlds[13] = new BatWorld(INVISIBLE, 1) ;

    setup(myWorlds,"nearTen");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = nearTen((Integer)w.getParameter(0));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean nearTen(int num) {
  /* BEGIN SOLUTION */
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
