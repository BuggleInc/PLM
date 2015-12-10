package lessons.welcome.bat.bool1;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class LastDigit extends BatExercise {
	public LastDigit(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "lastDigit");
		myWorld.addTest(VISIBLE, 7, 17) ;
		myWorld.addTest(VISIBLE, 6, 17) ;
		myWorld.addTest(VISIBLE, 3, 113) ;
		myWorld.addTest(INVISIBLE, 114, 113) ;
		myWorld.addTest(INVISIBLE, 114, 4) ;
		myWorld.addTest(INVISIBLE, 10, 0) ;
		myWorld.addTest(INVISIBLE, 11, 0) ;

		templatePython("lastDigit", new String[] {"Int", "Int"},
				"def lastDigit(a, b):\n",
				"   return a%10 == b%10\n");
		templateScala("lastDigit", new String[] {"Int", "Int"},
				"def lastDigit(a:Int, b:Int):Boolean = {\n",
				"   return a%10 == b%10\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( lastDigit((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean lastDigit(int a, int b) {
		/* BEGIN SOLUTION */
		return a%10 == b%10;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
