/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatWorld;

public class SortaSum extends BatExercise {
  public SortaSum(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[9];
    myWorlds[0] = new BatWorld(VISIBLE, 3, 4) ;
    myWorlds[1] = new BatWorld(VISIBLE, 9, 4) ;
    myWorlds[2] = new BatWorld(VISIBLE, 10, 11) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 12, -3) ;
    myWorlds[4] = new BatWorld(INVISIBLE, -3, 12) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 4, 5) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 4, 6) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 14, 7) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 14, 6) ;

    setup(myWorlds,"sortaSum");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = sortaSum((Integer)w.getParameter(0), (Integer)w.getParameter(1));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int sortaSum(int a, int b) {
  /* BEGIN SOLUTION */
    int sum = a+b;
    if (sum >= 10 && sum <= 19)
	   return 20;
    else
	   return sum;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
