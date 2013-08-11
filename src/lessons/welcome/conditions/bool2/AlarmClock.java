/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.conditions.bool2;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class AlarmClock extends BatExercise {
	public AlarmClock(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("alarmClock");
		myWorld.addTest(VISIBLE, 1, false) ;
		myWorld.addTest(VISIBLE, 5, false) ;
		myWorld.addTest(VISIBLE, 0, false) ;
		myWorld.addTest(INVISIBLE, 6, false) ;
		myWorld.addTest(INVISIBLE, 0, true) ;
		myWorld.addTest(INVISIBLE, 6, true) ;
		myWorld.addTest(INVISIBLE, 1, true) ;
		myWorld.addTest(INVISIBLE, 3, true) ;
		myWorld.addTest(INVISIBLE, 5, true) ;

		templatePython("alarmClock", 
				"def alarmClock(day, vacation):\n",
				"	if not vacation:\n"+
				"		if (day >= 1 and day <= 5):\n"+
				"			return '7:00'\n"+
				"		else:\n"+
				"			return '10:00'\n"+
				"	else:\n"+
				"		if (day >= 1 and day <= 5):\n"+
				"			return '10:00'\n"+
				"		else:\n"+
				"			return 'off'\n");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( alarmClock((Integer)t.getParameter(0), (Boolean)t.getParameter(1)) );
		/* END SKEL */
	}

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
