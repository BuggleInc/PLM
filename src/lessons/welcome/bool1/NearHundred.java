package lessons.welcome.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class NearHundred extends BatExercise {
	
	public NearHundred(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new BatWorld("NearHundred");
		myWorld.addTest(VISIBLE, 93);
		myWorld.addTest(VISIBLE, 90);
		myWorld.addTest(VISIBLE, 89);
		
		myWorld.addTest(INVISIBLE, 110);
		myWorld.addTest(INVISIBLE, 191);
		myWorld.addTest(INVISIBLE, 189);
		myWorld.addTest(INVISIBLE, 200);
		myWorld.addTest(INVISIBLE, 210);
		myWorld.addTest(INVISIBLE, 211);
		myWorld.addTest(INVISIBLE, -100);

		setup(myWorld);
	}


	/* BEGIN SKEL */
	public void run(BatTest t) {
		t.setResult( nearHundred((Integer)t.getParameter(0)) );		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean nearHundred(int n) {
	
		/* BEGIN SOLUTION */
	  return (90<=n && n<=110)||(190<=n&&n<=210);
		/* END SOLUTION */
}
		/* END TEMPLATE */
}
