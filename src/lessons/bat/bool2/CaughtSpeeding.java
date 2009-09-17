/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.bool2;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class CaughtSpeeding extends BatExercise {
  public CaughtSpeeding(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[12];
    myWorlds[0] = new BatWorld(VISIBLE, 60, false) ;
    myWorlds[1] = new BatWorld(VISIBLE, 65, false) ;
    myWorlds[2] = new BatWorld(VISIBLE, 65, true) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 80, false) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 85, false) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 85, true) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 70, false) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 75, false) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 75, true) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 40, false) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 40, true) ;
    myWorlds[11] = new BatWorld(INVISIBLE, 90, false) ;

    setup(myWorlds,"caughtSpeeding");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = caughtSpeeding((Integer)w.getParameter(0), (Boolean)w.getParameter(1));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int caughtSpeeding(int speed, boolean isBirthday) {
  /* BEGIN SOLUTION */
  if ((isBirthday && speed <= 65) || (speed <= 60))
     return 0;
  else if ((isBirthday && speed <= 85) || (speed <= 80))
     return 1;
  else 
     return 2;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
