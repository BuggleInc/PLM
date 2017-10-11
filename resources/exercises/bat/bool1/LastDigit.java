package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class LastDigit extends ExerciseTemplated {
	public LastDigit(Lesson lesson) {
		super("LastDigit");

		BatWorld myWorld = new BatWorld("lastDigit");
		myWorld.addTest(BatTest.VISIBLE, 7, 17) ;
		myWorld.addTest(BatTest.VISIBLE, 6, 17) ;
		myWorld.addTest(BatTest.VISIBLE, 3, 113) ;
		myWorld.addTest(BatTest.INVISIBLE, 114, 113) ;
		myWorld.addTest(BatTest.INVISIBLE, 114, 4) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 0) ;
		myWorld.addTest(BatTest.INVISIBLE, 11, 0) ;

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
