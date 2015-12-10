/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class MaxMod5 extends BatExercise {
	public MaxMod5(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "maxMod5");
		myWorld.addTest(VISIBLE, 2, 3) ;
		myWorld.addTest(VISIBLE, 6, 2) ;
		myWorld.addTest(VISIBLE, 3, 2) ;
		myWorld.addTest(INVISIBLE, 8, 12) ;
		myWorld.addTest(INVISIBLE, 7, 12) ;
		myWorld.addTest(INVISIBLE, 11, 6) ;
		myWorld.addTest(INVISIBLE, 2, 7) ;
		myWorld.addTest(INVISIBLE, 7, 7) ;
		myWorld.addTest(INVISIBLE, 9, 1) ;
		myWorld.addTest(INVISIBLE, 9, 14) ;
		myWorld.addTest(INVISIBLE, 1, 2) ;

		templatePython("maxMod5", new String[]{"Int","Int"},
				"def maxMod5(a, b):\n",
				"	if (a == b):\n"+
				"		return 0\n"+
				"	elif (a > b):\n"+
				"		if (a % 5 == b % 5):\n"+
				"			return b\n"+
				"		else:\n"+
				"			return a\n"+
				"	else:\n"+
				"		if (a % 5 == b % 5):\n"+
				"			return a\n"+
				"		else:\n"+
				"			return b");  
		templateScala("maxMod5", new String[]{"Int","Int"}, 
				"def maxMod5(a:Int, b:Int):Int = {\n",
				"	if (a == b)\n"+
				"		return 0\n"+
				"	else if (a > b) {\n"+
				"		if (a % 5 == b % 5)\n"+
				"			return b\n"+
				"		else\n"+
				"			return a\n"+
				"	} else\n"+
				"		if (a % 5 == b % 5)\n"+
				"			return a\n"+
				"		else\n"+
				"			return b\n"+
				"}");  

		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( maxMod5((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int maxMod5(int a, int b) {
		/* BEGIN SOLUTION */
		if (a == b)
			return 0;
		else if (a > b)
			if (a % 5 == b % 5)
				return b;
			else 
				return a;
		else 
			if (a % 5 == b % 5)
				return a;
			else 
				return b;  
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
