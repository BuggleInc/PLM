/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.logic1;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class WithoutDoubles extends BatExercise {
  public WithoutDoubles(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[12];
    myWorlds[0] = new BatWorld(VISIBLE, 2, 3, true) ;
    myWorlds[1] = new BatWorld(VISIBLE, 3, 3, true) ;
    myWorlds[2] = new BatWorld(VISIBLE, 3, 3, false) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 2, 3, false) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 5, 4, true) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 5, 4, false) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 5, 5, true) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 5, 5, false) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 6, 6, true) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 6, 6, false) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 1, 6, true) ;
    myWorlds[11] = new BatWorld(INVISIBLE, 6, 1, false) ;

    setup(myWorlds,"withoutDoubles");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = withoutDoubles((Integer)w.getParameter(0), (Integer)w.getParameter(1), (Boolean)w.getParameter(2));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int withoutDoubles(int die1, int die2, boolean noDoubles) {
  /* BEGIN SOLUTION */
  if (noDoubles && (die1 == die2)) {
     if (die1 == 6)
        return 1 + die2;
     else 
        return die1 + 1 + die2;
   } else 
		return die1 + die2;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
