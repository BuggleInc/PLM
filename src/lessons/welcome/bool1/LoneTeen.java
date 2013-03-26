package lessons.welcome.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class LoneTeen extends BatExercise {
	
	public LoneTeen(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new BatWorld("LoneTeen");
		myWorld.addTest(VISIBLE,  13,42);
		myWorld.addTest(VISIBLE,  21,19);
		myWorld.addTest(VISIBLE,  13,13);
		
		myWorld.addTest(INVISIBLE, 14,20);
		myWorld.addTest(INVISIBLE, 20,15);
		myWorld.addTest(INVISIBLE, 16,17);
		myWorld.addTest(INVISIBLE, 16,9);
		myWorld.addTest(INVISIBLE, 16,18);
		myWorld.addTest(INVISIBLE, 13,19);
		myWorld.addTest(INVISIBLE, 13,20);
		myWorld.addTest(INVISIBLE, 6,18);
		myWorld.addTest(INVISIBLE, 42,13);
		myWorld.addTest(INVISIBLE, 42,42);

		setup(myWorld);
	}


	/* BEGIN SKEL */
	public void run(BatTest t) {
		t.setResult( loneTeen((Integer)t.getParameter(0),(Integer)t.getParameter(1)) );		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean loneTeen(int a, int b) {
	/* BEGIN SOLUTION */
	boolean teenA = a>12&&a<20;
	boolean teenB = b>12&&b<20;
	return  (teenA&&!teenB) || (teenB&&!teenA);
	/* END SOLUTION */
}
		/* END TEMPLATE */
}
