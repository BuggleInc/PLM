package lessons.welcome.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class Diff21 extends BatExercise {
	
	public Diff21(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new BatWorld("Diff21");
		myWorld.addTest(VISIBLE,  2);
		myWorld.addTest(VISIBLE,  11);
		myWorld.addTest(VISIBLE,  0);
		
		myWorld.addTest(INVISIBLE, 19);
		myWorld.addTest(INVISIBLE, 10);
		myWorld.addTest(INVISIBLE, 21);
		myWorld.addTest(INVISIBLE, 22);
		myWorld.addTest(INVISIBLE, 25);
		myWorld.addTest(INVISIBLE, 30);
		myWorld.addTest(INVISIBLE, -21);

		setup(myWorld);
	}


	/* BEGIN SKEL */
	public void run(BatTest t) {
		t.setResult( diff21((Integer)t.getParameter(0)) );		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
int diff21(int n) {
	/* BEGIN SOLUTION */
	  if (n>21)
		  return 2*(n-21);
	  return 21-n;
			/* END SOLUTION */
}
		/* END TEMPLATE */
}
