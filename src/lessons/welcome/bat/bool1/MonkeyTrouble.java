package lessons.welcome.bat.bool1;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class MonkeyTrouble extends BatExercise {

	public MonkeyTrouble(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "monkeyTrouble");
		myWorld.addTest(VISIBLE, true, true);
		myWorld.addTest(VISIBLE, false, false);
		myWorld.addTest(VISIBLE, true, false);
		myWorld.addTest(INVISIBLE, false, true);

		templatePython("monkeyTrouble", new String[] {"Boolean","Boolean"},
				"def monkeyTrouble(aSmile, bSmile):\n",
				"   return (aSmile and bSmile) or (not aSmile and not bSmile)\n");
		templateScala("monkeyTrouble", new String[] {"Boolean","Boolean"}, 
				"def monkeyTrouble(aSmile:Boolean, bSmile:Boolean): Boolean = {\n",
				"   return ((aSmile && bSmile) || (!aSmile && !bSmile))\n"
		  	  + "}");

		setup(myWorld);
	}


	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( monkeyTrouble((Boolean)t.getParameter(0),(Boolean)t.getParameter(1)) );		
		/* END SKEL */
	}

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
