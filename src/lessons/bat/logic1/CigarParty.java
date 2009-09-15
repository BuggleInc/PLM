/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.logic1;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class CigarParty extends BatExercise {
  public CigarParty(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[11];
    myWorlds[0] = new BatWorld(VISIBLE, 30, false) ;
    myWorlds[1] = new BatWorld(VISIBLE, 50, false) ;
    myWorlds[2] = new BatWorld(VISIBLE, 70, true) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 30, true) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 50, true) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 60, false) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 61, false) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 40, false) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 39, false) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 40, true) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 39, true) ;

    setup(myWorlds,"cigarParty");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = cigarParty((Integer)w.getParameter(0), (Boolean)w.getParameter(1));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean cigarParty(int cigars, boolean isWeekend) {
  /* BEGIN SOLUTION */
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
