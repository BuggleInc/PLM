/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class BlueTicket extends BatExercise {
	public BlueTicket(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "blueTicket");
		myWorld.addTest(VISIBLE, 9, 1, 0) ;
		myWorld.addTest(VISIBLE, 9, 2, 0) ;
		myWorld.addTest(VISIBLE, 6, 1, 4) ;
		myWorld.addTest(INVISIBLE, 6, 1, 5) ;
		myWorld.addTest(INVISIBLE, 10, 0, 0) ;
		myWorld.addTest(INVISIBLE, 15, 0, 5) ;
		myWorld.addTest(INVISIBLE, 5, 15, 5) ;
		myWorld.addTest(INVISIBLE, 4, 11, 1) ;
		myWorld.addTest(INVISIBLE, 13, 2, 3) ;
		myWorld.addTest(INVISIBLE, 8, 4, 3) ;
		myWorld.addTest(INVISIBLE, 8, 4, 2) ;
		myWorld.addTest(INVISIBLE, 8, 4, 1) ;

		templatePython("blueTicket", new String[]{"Int","Int","Int"},
				"def blueTicket(a, b, c):\n",
				"	ab = a + b\n"+
				"	ac = a + c\n"+
				"	bc = b + c\n"+
				"	if (ab == 10 or ac == 10 or bc == 10):\n"+
				"		return 10\n"+
				"	elif (ab == (bc + 10) or ab == (ac + 10)):\n"+
				"		return 5\n"+
				"	else:\n"+
				"		return 0\n");
		templateScala("blueTicket",new String[]{"Int","Int","Int"}, 
				"def blueTicket(a:Int, b:Int, c:Int):Int = {\n",
				"	val ab = a + b\n"+
				"	val ac = a + c\n"+
				"	val bc = b + c\n"+
				"	if (ab == 10 || ac == 10 || bc == 10)\n"+
				"		return 10\n"+
				"	else if (ab == (bc + 10) || ab == (ac + 10))\n"+
				"		return 5\n"+
				"	else\n"+
				"		return 0\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( blueTicket((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int blueTicket(int a, int b, int c) {
		/* BEGIN SOLUTION */
		int ab = a + b;
		int ac = a + c;
		int bc = b + c;

		if (ab == 10 || ac == 10 || bc == 10)
			return 10;
		else if (ab == (bc + 10) || ab == (ac + 10))
			return 5;
		else 
			return 0;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
