/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class AlarmClock extends BatExercise {
  public AlarmClock(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("AlarmClock");
    myWorld.addTest(VISIBLE, 1, false) ;
    myWorld.addTest(VISIBLE, 5, false) ;
    myWorld.addTest(VISIBLE, 0, false) ;
    myWorld.addTest(INVISIBLE, 6, false) ;
    myWorld.addTest(INVISIBLE, 0, true) ;
    myWorld.addTest(INVISIBLE, 6, true) ;
    myWorld.addTest(INVISIBLE, 1, true) ;
    myWorld.addTest(INVISIBLE, 3, true) ;
    myWorld.addTest(INVISIBLE, 5, true) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( alarmClock((Integer)t.getParameter(0), (Boolean)t.getParameter(1)) );
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
