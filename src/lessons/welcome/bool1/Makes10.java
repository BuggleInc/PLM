package lessons.welcome.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class Makes10 extends BatExercise {
	
	public Makes10(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new BatWorld("Makes10");
		myWorld.addTest(VISIBLE,  9,10);
		myWorld.addTest(VISIBLE,  9,9);
		myWorld.addTest(VISIBLE,  1,9);
		
		myWorld.addTest(INVISIBLE, 10,1);
		myWorld.addTest(INVISIBLE, 10,10);
		myWorld.addTest(INVISIBLE, 8,2);
		myWorld.addTest(INVISIBLE, 8,3);
		myWorld.addTest(INVISIBLE, 10,42);
		myWorld.addTest(INVISIBLE, 12,-2);

		setup(myWorld);
	}


	/* BEGIN SKEL */
	public void run(BatTest t) {
		t.setResult( makes10((Integer)t.getParameter(0),(Integer)t.getParameter(1)) );		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean makes10(int a, int b) {
	/* BEGIN SOLUTION */
		  return a==10||b==10||(a+b)==10;
			/* END SOLUTION */
}
		/* END TEMPLATE */
}
