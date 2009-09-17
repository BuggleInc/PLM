package lessons.bat.bool1;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class LastDigit extends BatExercise {
  public LastDigit(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[7];
    myWorlds[0] = new BatWorld(VISIBLE, 7, 17) ;
    myWorlds[1] = new BatWorld(VISIBLE, 6, 17) ;
    myWorlds[2] = new BatWorld(VISIBLE, 3, 113) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 114, 113) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 114, 4) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 10, 0) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 11, 0) ;

    setup(myWorlds,"lastDigit");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = lastDigit((Integer)w.getParameter(0), (Integer)w.getParameter(1));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
boolean lastDigit(int a, int b) {
  /* BEGIN SOLUTION */
	  return a%10 == b%10;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
