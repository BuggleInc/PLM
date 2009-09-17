/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.bool2;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class SquirrelPlay extends BatExercise {
  public SquirrelPlay(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[13];
    myWorlds[0] = new BatWorld(VISIBLE, 70, false) ;
    myWorlds[1] = new BatWorld(VISIBLE, 95, false) ;
    myWorlds[2] = new BatWorld(VISIBLE, 95, true) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 90, false) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 90, true) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 50, false) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 50, true) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 100, false) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 100, true) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 105, true) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 59, false) ;
    myWorlds[11] = new BatWorld(INVISIBLE, 59, true) ;
    myWorlds[12] = new BatWorld(INVISIBLE, 60, false) ;

    setup(myWorlds,"squirrelPlay");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = squirrelPlay((Integer)w.getParameter(0), (Boolean)w.getParameter(1));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean squirrelPlay(int temp, boolean isSummer) {
  /* BEGIN SOLUTION */
  return (temp >= 60 && ((isSummer && temp <= 100) || temp <= 90));
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
