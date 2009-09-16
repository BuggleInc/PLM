/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.logic1;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class In1To10 extends BatExercise {
  public In1To10(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[12];
    myWorlds[0] = new BatWorld(VISIBLE, 5, false) ;
    myWorlds[1] = new BatWorld(VISIBLE, 11, false) ;
    myWorlds[2] = new BatWorld(VISIBLE, 11, true) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 10, false) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 10, true) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 9, false) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 9, true) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 1, false) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 1, true) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 0, false) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 0, true) ;
    myWorlds[11] = new BatWorld(INVISIBLE, -1, false) ;

    setup(myWorlds,"in1To10");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = in1To10((Integer)w.getParameter(0), (Boolean)w.getParameter(1));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean in1To10(int n, boolean outsideMode) {
  /* BEGIN SOLUTION */
  return (outsideMode && (n <= 1 || n >= 10)) || ((! outsideMode) && (n >= 1 && n <= 10));
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
