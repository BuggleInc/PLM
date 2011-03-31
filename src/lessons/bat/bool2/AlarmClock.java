/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.bat.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatWorld;

public class AlarmClock extends BatExercise {
  public AlarmClock(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[9];
    myWorlds[0] = new BatWorld(VISIBLE, 1, false) ;
    myWorlds[1] = new BatWorld(VISIBLE, 5, false) ;
    myWorlds[2] = new BatWorld(VISIBLE, 0, false) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 6, false) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 0, true) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 6, true) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 1, true) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 3, true) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 5, true) ;

    setup(myWorlds,"alarmClock");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = alarmClock((Integer)w.getParameter(0), (Boolean)w.getParameter(1));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
String alarmClock(int day, boolean vacation) {
  /* BEGIN SOLUTION */
	  if (! vacation) {  
		     if (day >= 1 && day <= 5)
		        return "7:00";
		     else
		        return "10:00";
		  } else {
		     if (day >= 1 && day <= 5)
		        return "10:00";
		     else
		        return "off";  
		  }
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
