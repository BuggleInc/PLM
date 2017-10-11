/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class AlarmClock extends ExerciseTemplated {
	public AlarmClock(Lesson lesson) {
		super("AlarmClock");

		BatWorld myWorld = new BatWorld("alarmClock");
		myWorld.addTest(BatTest.VISIBLE, 1, false) ;
		myWorld.addTest(BatTest.VISIBLE, 5, false) ;
		myWorld.addTest(BatTest.VISIBLE, 0, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 6, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 0, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 6, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, true) ;

		templatePython("alarmClock", new String[]{"int","bool"},
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
		templateScala("alarmClock", new String[] {"Int","Boolean"},
				"def alarmClock(day:Int, vacation:Boolean): String = {\n",
				  "  if (! vacation) {\n"
				+ "    if (day >= 1 && day <= 5) {\n"
				+ "      return \"7:00\"\n"
				+ "    } else {\n"
				+ "      return \"10:00\"\n"
				+ "    }\n"
				+ "  } else {\n"
				+ "    if (day >= 1 && day <= 5) {\n"
				+ "      return \"10:00\"\n"
				+ "    } else {\n"
				+ "      return \"off\"\n"
				+ "    }\n"
				+ "  }\n"
				+ "}");

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
