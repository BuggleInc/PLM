package lessons.welcome.bat.bool1;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ParotTrouble extends BatExercise {

	public ParotTrouble(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "parotTrouble");
		myWorld.addTest(VISIBLE,  true,6);
		myWorld.addTest(VISIBLE,  true,7);
		myWorld.addTest(VISIBLE,  false,6);

		myWorld.addTest(INVISIBLE, true,21);
		myWorld.addTest(INVISIBLE, false,21);
		myWorld.addTest(INVISIBLE, true,23);
		myWorld.addTest(INVISIBLE, false,23);
		myWorld.addTest(INVISIBLE, true,20);


		templatePython("parotTrouble", new String[] {"Boolean","Int"},
				"def parotTrouble(talking, hour):\n",
				"   return (talking and (hour<7 or hour>20))\n");
		templateScala("parotTrouble", new String[] {"Boolean","Int"},
				"def parotTrouble(talking:Boolean, hour:Int):Boolean = {\n",
				"  return (talking && (hour<7||hour>20))\n"
			  + "}");
		setup(myWorld);
	}


	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( parotTrouble((Boolean)t.getParameter(0),(Integer)t.getParameter(1)) );		
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean parotTrouble(boolean talking, int hour) {
		/* BEGIN SOLUTION */
		return (talking && (hour<7||hour>20));	
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
