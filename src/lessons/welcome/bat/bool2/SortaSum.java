/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class SortaSum extends BatExercise {
	public SortaSum(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "sortaSum");
		myWorld.addTest(VISIBLE, 3, 4) ;
		myWorld.addTest(VISIBLE, 9, 4) ;
		myWorld.addTest(VISIBLE, 10, 11) ;
		myWorld.addTest(INVISIBLE, 12, -3) ;
		myWorld.addTest(INVISIBLE, -3, 12) ;
		myWorld.addTest(INVISIBLE, 4, 5) ;
		myWorld.addTest(INVISIBLE, 4, 6) ;
		myWorld.addTest(INVISIBLE, 14, 7) ;
		myWorld.addTest(INVISIBLE, 14, 6) ;

		templatePython("sortaSum", new String[]{"Int","Int"},
				"def sortaSum(a, b):\n",
				"	sum = a+b\n"+
				"	if (sum >= 10 and sum <= 19):\n"+
				"		return 20\n"+
				"	else:\n"+
				"		return sum\n");
		templateScala("sortaSum", new String[]{"Int","Int"},
				"def sortaSum(a:Int, b:Int):Int = {\n",
				"	val sum = a+b\n"+
				"	if (sum >= 10 && sum <= 19)\n"+
				"		return 20\n"+
				"	else\n"+
				"		return sum\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( sortaSum((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int sortaSum(int a, int b) {
		/* BEGIN SOLUTION */
		int sum = a+b;
		if (sum >= 10 && sum <= 19)
			return 20;
		else
			return sum;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
