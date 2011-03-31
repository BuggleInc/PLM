/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatWorld;

public class TwoAsOne extends BatExercise {
  public TwoAsOne(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[12];
    myWorlds[0] = new BatWorld(VISIBLE, 1, 2, 3) ;
    myWorlds[1] = new BatWorld(VISIBLE, 3, 1, 2) ;
    myWorlds[2] = new BatWorld(VISIBLE, 3, 2, 2) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 2, 3, 1) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 5, 3, -2) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 5, 3, -3) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 2, 5, 3) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 9, 5, 5) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 9, 4, 5) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 5, 4, 9) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 3, 3, 0) ;
    myWorlds[11] = new BatWorld(INVISIBLE, 3, 3, 2) ;

    setup(myWorlds,"twoAsOne");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = twoAsOne((Integer)w.getParameter(0), (Integer)w.getParameter(1), (Integer)w.getParameter(2));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean twoAsOne(int a, int b, int c) {
  /* BEGIN SOLUTION */
  return (a + b == c) || (a + c == b) || (b + c == a);
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
