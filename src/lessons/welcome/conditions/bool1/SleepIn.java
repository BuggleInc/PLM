package lessons.welcome.conditions.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class SleepIn extends BatExercise {

	public SleepIn(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("sleepIn");
		myWorld.addTest(VISIBLE,  false,false);
		myWorld.addTest(VISIBLE,  true,false);
		myWorld.addTest(INVISIBLE, false,true);
		myWorld.addTest(INVISIBLE, true,true);

		templatePython("sleepIn", 
				"def sleepIn(weekday, vacation):\n",
				"    return not weekday or vacation\n");
		templateScala("sleepIn", new String[] {"Boolean","Boolean"},
				"def sleepIn(weekday:Boolean, vacation:Boolean): Boolean = {\n",
				"  return !weekday || vacation;\n"
				+ "}\n");
		setup(myWorld);
	}


	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( sleepIn((Boolean)t.getParameter(0),(Boolean)t.getParameter(1)) );		
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean sleepIn(boolean weekday, boolean vacation) {
		/* BEGIN SOLUTION */
		return !weekday || vacation;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
