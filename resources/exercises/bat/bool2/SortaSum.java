/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class SortaSum extends ExerciseTemplated {
	public SortaSum(Lesson lesson) {
		super("SortaSum");

		BatWorld myWorld = new BatWorld("sortaSum");
		myWorld.addTest(BatTest.VISIBLE, 3, 4) ;
		myWorld.addTest(BatTest.VISIBLE, 9, 4) ;
		myWorld.addTest(BatTest.VISIBLE, 10, 11) ;
		myWorld.addTest(BatTest.INVISIBLE, 12, -3) ;
		myWorld.addTest(BatTest.INVISIBLE, -3, 12) ;
		myWorld.addTest(BatTest.INVISIBLE, 4, 5) ;
		myWorld.addTest(BatTest.INVISIBLE, 4, 6) ;
		myWorld.addTest(BatTest.INVISIBLE, 14, 7) ;
		myWorld.addTest(BatTest.INVISIBLE, 14, 6) ;

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
