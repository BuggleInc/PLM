/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.logic1;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class TeenSum extends BatExercise {
  public TeenSum(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[16];
    myWorlds[0] = new BatWorld(VISIBLE, 3, 4) ;
    myWorlds[1] = new BatWorld(VISIBLE, 10, 13) ;
    myWorlds[2] = new BatWorld(VISIBLE, 13, 2) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 3, 19) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 13, 13) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 10, 10) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 6, 14) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 15, 2) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 19, 19) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 19, 20) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 2, 18) ;
    myWorlds[11] = new BatWorld(INVISIBLE, 12, 4) ;
    myWorlds[12] = new BatWorld(INVISIBLE, 2, 20) ;
    myWorlds[13] = new BatWorld(INVISIBLE, 2, 17) ;
    myWorlds[14] = new BatWorld(INVISIBLE, 2, 16) ;
    myWorlds[15] = new BatWorld(INVISIBLE, 6, 7) ;

    setup(myWorlds,"teenSum");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = teenSum((Integer)w.getParameter(0), (Integer)w.getParameter(1));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int teenSum(int a, int b) {
  /* BEGIN SOLUTION */
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
