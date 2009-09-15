/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.logic1;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class TeaParty extends BatExercise {
  public TeaParty(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[13];
    myWorlds[0] = new BatWorld(VISIBLE, 6, 8) ;
    myWorlds[1] = new BatWorld(VISIBLE, 3, 8) ;
    myWorlds[2] = new BatWorld(VISIBLE, 20, 6) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 12, 6) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 11, 6) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 11, 4) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 4, 5) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 5, 5) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 6, 6) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 5, 10) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 5, 9) ;
    myWorlds[11] = new BatWorld(INVISIBLE, 10, 4) ;
    myWorlds[12] = new BatWorld(INVISIBLE, 10, 20) ;

    setup(myWorlds,"teaParty");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = teaParty((Integer)w.getParameter(0), (Integer)w.getParameter(1));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int teaParty(int tea, int candy) {
  /* BEGIN SOLUTION */
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
