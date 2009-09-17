/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.bool2;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class ShareDigit extends BatExercise {
  public ShareDigit(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[10];
    myWorlds[0] = new BatWorld(VISIBLE, 12, 23) ;
    myWorlds[1] = new BatWorld(VISIBLE, 12, 43) ;
    myWorlds[2] = new BatWorld(VISIBLE, 12, 44) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 23, 12) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 23, 39) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 23, 19) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 30, 90) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 30, 91) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 55, 55) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 55, 44) ;

    setup(myWorlds,"shareDigit");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = shareDigit((Integer)w.getParameter(0), (Integer)w.getParameter(1));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean shareDigit(int a, int b) {
  /* BEGIN SOLUTION */
  return (a/10 == b/10 || a/10 == b%10 || a%10 == b/10 || a%10 == b%10);
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
