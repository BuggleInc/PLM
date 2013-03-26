package lessons.welcome.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class MonkeyTrouble extends BatExercise {
	
	public MonkeyTrouble(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new BatWorld("MonkeyTrouble");
		myWorld.addTest(VISIBLE, true, true);
		myWorld.addTest(VISIBLE, false, false);
		myWorld.addTest(VISIBLE, true, false);
		myWorld.addTest(INVISIBLE, false, true);
		
		setup(myWorld);
	}


	/* BEGIN SKEL */
	public void run(BatTest t) {
		t.setResult( monkeyTrouble((Boolean)t.getParameter(0),(Boolean)t.getParameter(1)) );		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
public boolean monkeyTrouble(boolean aSmile, boolean bSmile) {
	/* BEGIN SOLUTION */
	  if (aSmile && bSmile) {
		    return true;
		  }
		  if (!aSmile && !bSmile) {
		    return true;
		  }
		  return false;
    // This all can be shortened to just:
	// return ((aSmile && bSmile) || (!aSmile && !bSmile));
	/* END SOLUTION */		  
}	
	/* END TEMPLATE */
}
