package lessons.welcome.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class HasTeen extends BatExercise {
	
	public HasTeen(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new BatWorld("HasTeen");
		myWorld.addTest(VISIBLE,  13,20,10);		
		myWorld.addTest(VISIBLE,  20,19,10);
		myWorld.addTest(VISIBLE,  20,10,13);

		myWorld.addTest(INVISIBLE, 1,20,12);
		myWorld.addTest(INVISIBLE, 19,20,12);
		myWorld.addTest(INVISIBLE, 12,20,19);
		myWorld.addTest(INVISIBLE, 12,9,20);
		myWorld.addTest(INVISIBLE, 12,18,20);
		myWorld.addTest(INVISIBLE, 14,2,20);
		myWorld.addTest(INVISIBLE, 4,2,20);
		myWorld.addTest(INVISIBLE, 11,22,22);


		setup(myWorld);
	}


	/* BEGIN SKEL */
	public void run(BatTest t) {
		t.setResult( hasTeen((Integer)t.getParameter(0),(Integer)t.getParameter(1),(Integer)t.getParameter(2)) );		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean hasTeen(int a, int b, int c) {
	/* BEGIN SOLUTION */
		  return a>12&&a<20 || b>12&&b<20 || c>12&&c<20;
	/* END SOLUTION */
}
		/* END TEMPLATE */
}
