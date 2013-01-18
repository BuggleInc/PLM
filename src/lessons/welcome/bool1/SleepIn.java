package lessons.welcome.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class SleepIn extends BatExercise {
	
	public SleepIn(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new BatWorld("SleepIn");
		myWorld.addTest(VISIBLE,  false,false);
		myWorld.addTest(VISIBLE,  true,false);
		myWorld.addTest(INVISIBLE, false,true);
		myWorld.addTest(INVISIBLE, true,true);

		setup(myWorld);
	}


	/* BEGIN SKEL */
	public void run(BatTest t) {
		t.setResult( sleepIn((Boolean)t.getParameter(0),(Boolean)t.getParameter(1)) );		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean sleepIn(boolean weekday, boolean vacation) {
		/* BEGIN SOLUTION */
		 if (!weekday || vacation) {
			 return true;
		 } else {
			 return false;
		 }
		 // This can be shortened to: return(!weekday || vacation);
		/* END SOLUTION */
}
		/* END TEMPLATE */
}
