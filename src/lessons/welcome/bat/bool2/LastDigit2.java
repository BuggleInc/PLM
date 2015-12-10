/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class LastDigit2 extends BatExercise {
	public LastDigit2(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "lastDigit");
		myWorld.addTest(VISIBLE, 23, 19, 13) ;
		myWorld.addTest(VISIBLE, 23, 19, 12) ;
		myWorld.addTest(VISIBLE, 23, 19, 3) ;
		myWorld.addTest(INVISIBLE, 23, 19, 39) ;
		myWorld.addTest(INVISIBLE, 1, 2, 3) ;
		myWorld.addTest(INVISIBLE, 1, 1, 2) ;
		myWorld.addTest(INVISIBLE, 1, 2, 2) ;
		myWorld.addTest(INVISIBLE, 14, 25, 43) ;
		myWorld.addTest(INVISIBLE, 14, 25, 45) ;
		myWorld.addTest(INVISIBLE, 248, 106, 1002) ;
		myWorld.addTest(INVISIBLE, 248, 106, 1008) ;
		myWorld.addTest(INVISIBLE, 10, 11, 20) ;
		myWorld.addTest(INVISIBLE, 0, 11, 0) ;

		templatePython("lastDigit", new String[]{"Int","Int","Int"},
				"def lastDigit(a, b, c):\n",
				"	da = a % 10\n"+
				"	db = b % 10\n"+
				"	dc = c % 10\n"+
				"	return da == db or da == dc or dc == db\n");
		templateScala("lastDigit",new String[]{"Int","Int","Int"}, 
				"def lastDigit(a:Int, b:Int, c:Int):Boolean = {\n",
				"	val da = a % 10\n"+
				"	val db = b % 10\n"+
				"	val dc = c % 10\n"+
				"	return da == db || da == dc || dc == db\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( lastDigit((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean lastDigit(int a, int b, int c) {
		/* BEGIN SOLUTION */
		int da = a % 10;
		int db = b % 10;
		int dc = c % 10;
		return da == db || da == dc || dc == db;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
