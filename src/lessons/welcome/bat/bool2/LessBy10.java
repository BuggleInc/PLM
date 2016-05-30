/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class LessBy10 extends BatExercise {
	public LessBy10(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "lessBy10");
		myWorld.addTest(VISIBLE, 1, 7, 11) ;
		myWorld.addTest(VISIBLE, 1, 7, 10) ;
		myWorld.addTest(VISIBLE, 11, 1, 7) ;
		myWorld.addTest(INVISIBLE, 10, 7, 1) ;
		myWorld.addTest(INVISIBLE, -10, 2, 2) ;
		myWorld.addTest(INVISIBLE, 2, 11, 11) ;
		myWorld.addTest(INVISIBLE, 3, 3, 30) ;
		myWorld.addTest(INVISIBLE, 3, 3, 3) ;
		myWorld.addTest(INVISIBLE, 10, 1, 11) ;
		myWorld.addTest(INVISIBLE, 10, 11, 1) ;
		myWorld.addTest(INVISIBLE, 10, 11, 2) ;
		myWorld.addTest(INVISIBLE, 3, 30, 3) ;
		myWorld.addTest(INVISIBLE, 2, 2, -8) ;
		myWorld.addTest(INVISIBLE, 2, 8, 12) ;

		templatePython("lessBy10", new String[] {"Int","Int","Int"},
				"def lessBy10(a, b, c):\n",
				"	return ((a - b) >= 10) or ((b - a) >= 10) or ((b - c) >= 10) or ((c - b) >= 10) or ((a - c) >= 10) or ((c - a) >= 10)\n");
		templateScala("lessBy10", new String[] {"Int","Int","Int"},
				"def lessBy10(a:Int, b:Int, c:Int):Boolean = {\n",
				"	return ((a - b) >= 10) || ((b - a) >= 10) || ((b - c) >= 10) || ((c - b) >= 10) || ((a - c) >= 10) || ((c - a) >= 10)\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( lessBy10((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean lessBy10(int a, int b, int c) {
		/* BEGIN SOLUTION */
		return ((a - b) >= 10) || ((b - a) >= 10) || ((b - c) >= 10) || ((c - b) >= 10) || ((a - c) >= 10) || ((c - a) >= 10);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
