/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.bool2;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class GreenTicket extends BatExercise {
  public GreenTicket(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[12];
    myWorlds[0] = new BatWorld(VISIBLE, 1, 2, 3) ;
    myWorlds[1] = new BatWorld(VISIBLE, 2, 2, 2) ;
    myWorlds[2] = new BatWorld(VISIBLE, 1, 1, 2) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 2, 1, 1) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 1, 2, 1) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 3, 2, 1) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 0, 0, 0) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 2, 0, 0) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 0, 9, 10) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 0, 10, 0) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 9, 9, 9) ;
    myWorlds[11] = new BatWorld(INVISIBLE, 9, 0, 9) ;

    setup(myWorlds,"greenTicket");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = greenTicket((Integer)w.getParameter(0), (Integer)w.getParameter(1), (Integer)w.getParameter(2));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int greenTicket(int a, int b, int c) {
  /* BEGIN SOLUTION */
  if (a == b && b == c)
     return 20;
  else if (a == b || b == c || a == c)
     return 10;
  else  // (a != b && b != a && c != a)
     return 0;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
