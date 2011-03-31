/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatWorld;

public class InOrderEqual extends BatExercise {
  public InOrderEqual(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[14];
    myWorlds[0] = new BatWorld(VISIBLE, 2, 5, 11, false) ;
    myWorlds[1] = new BatWorld(VISIBLE, 5, 7, 6, false) ;
    myWorlds[2] = new BatWorld(VISIBLE, 5, 5, 7, true) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 5, 5, 7, false) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 2, 5, 4, false) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 3, 4, 3, false) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 3, 4, 4, false) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 3, 4, 3, true) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 3, 4, 4, true) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 1, 5, 5, true) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 5, 5, 5, true) ;
    myWorlds[11] = new BatWorld(INVISIBLE, 2, 2, 1, true) ;
    myWorlds[12] = new BatWorld(INVISIBLE, 9, 2, 2, true) ;
    myWorlds[13] = new BatWorld(INVISIBLE, 0, 1, 0, true) ;

    setup(myWorlds,"inOrderEqual");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = inOrderEqual((Integer)w.getParameter(0), (Integer)w.getParameter(1), (Integer)w.getParameter(2), (Boolean)w.getParameter(3));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean inOrderEqual(int a, int b, int c, boolean equalOk) {
  /* BEGIN SOLUTION */
  return (equalOk && ((a <= b) && (b <= c))) || (a < b && b < c);
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
