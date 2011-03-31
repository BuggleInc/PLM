/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatWorld;

public class RedTicket extends BatExercise {
  public RedTicket(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[11];
    myWorlds[0] = new BatWorld(VISIBLE, 2, 2, 2) ;
    myWorlds[1] = new BatWorld(VISIBLE, 2, 2, 1) ;
    myWorlds[2] = new BatWorld(VISIBLE, 0, 0, 0) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 2, 0, 0) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 1, 1, 1) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 1, 2, 1) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 1, 2, 0) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 0, 2, 2) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 1, 2, 2) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 0, 2, 0) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 1, 1, 2) ;

    setup(myWorlds,"redTicket");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = redTicket((Integer)w.getParameter(0), (Integer)w.getParameter(1), (Integer)w.getParameter(2));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int redTicket(int a, int b, int c) {
  /* BEGIN SOLUTION */
  if (a == b && b == c && c == 2)
     return 10;
  else if (a == b && b == c)
     return 5;
  else if (b != a && c != a)
	 return 1;
  else
     return 0;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
