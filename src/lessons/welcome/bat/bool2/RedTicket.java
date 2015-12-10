/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class RedTicket extends BatExercise {
	public RedTicket(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "redTicket");
		myWorld.addTest(VISIBLE, 2, 2, 2) ;
		myWorld.addTest(VISIBLE, 2, 2, 1) ;
		myWorld.addTest(VISIBLE, 0, 0, 0) ;
		myWorld.addTest(INVISIBLE, 2, 0, 0) ;
		myWorld.addTest(INVISIBLE, 1, 1, 1) ;
		myWorld.addTest(INVISIBLE, 1, 2, 1) ;
		myWorld.addTest(INVISIBLE, 1, 2, 0) ;
		myWorld.addTest(INVISIBLE, 0, 2, 2) ;
		myWorld.addTest(INVISIBLE, 1, 2, 2) ;
		myWorld.addTest(INVISIBLE, 0, 2, 0) ;
		myWorld.addTest(INVISIBLE, 1, 1, 2) ;

		templatePython("redTicket", new String[]{"Int","Int","Int"},
				"def redTicket(a, b, c):\n",
				"	if (a == b and b == c and c == 2):\n"+
				"		return 10\n"+
				"	elif (a == b and b == c):\n"+
				"		return 5\n"+
				"	elif (b != a and c != a):\n"+
				"		return 1\n"+
				"	else:\n"+
				"		return 0\n");
		templateScala("redTicket", new String[]{"Int","Int","Int"}, 
				"def redTicket(a:Int, b:Int, c:Int):Int = {\n",
				"	if (a == b && b == c && c == 2)\n"+
				"		return 10\n"+
				"	else if (a == b && b == c)\n"+
				"		return 5\n"+
				"	else if (b != a && c != a)\n"+
				"		return 1\n"+
				"	else\n"+
				"		return 0\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( redTicket((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int redTicket(int a, int b, int c) {
		/* BEGIN SOLUTION */
		if (a == b && b == c && c == 2)
			return 10;
		else if (a == b && b == c)
			return 5;
		else if (b != a && c != a)
			return 1;
		else
			return 0;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
