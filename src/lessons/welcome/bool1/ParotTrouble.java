package lessons.welcome.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class ParotTrouble extends BatExercise {
	
	public ParotTrouble(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new BatWorld("ParotTrouble");
		myWorld.addTest(VISIBLE,  true,6);
		myWorld.addTest(VISIBLE,  true,7);
		myWorld.addTest(VISIBLE,  false,6);
		
		myWorld.addTest(INVISIBLE, true,21);
		myWorld.addTest(INVISIBLE, false,21);
		myWorld.addTest(INVISIBLE, true,23);
		myWorld.addTest(INVISIBLE, false,23);
		myWorld.addTest(INVISIBLE, true,20);


		setup(myWorld);
	}


	/* BEGIN SKEL */
	public void run(BatTest t) {
		t.setResult( parotTrouble((Boolean)t.getParameter(0),(Integer)t.getParameter(1)) );		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean parotTrouble(boolean talking, int hour) {
		/* BEGIN SOLUTION */
	  return (talking && (hour<7||hour>20));	
		/* END SOLUTION */
}
		/* END TEMPLATE */
}
