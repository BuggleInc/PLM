/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class TeaParty extends ExerciseTemplated {
	public TeaParty(Lesson lesson) {
		super("TeaParty");

		BatWorld myWorld = new BatWorld("teaParty");
		myWorld.addTest(BatTest.VISIBLE, 6, 8) ;
		myWorld.addTest(BatTest.VISIBLE, 3, 8) ;
		myWorld.addTest(BatTest.VISIBLE, 20, 6) ;
		myWorld.addTest(BatTest.INVISIBLE, 12, 6) ;
		myWorld.addTest(BatTest.INVISIBLE, 11, 6) ;
		myWorld.addTest(BatTest.INVISIBLE, 11, 4) ;
		myWorld.addTest(BatTest.INVISIBLE, 4, 5) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 5) ;
		myWorld.addTest(BatTest.INVISIBLE, 6, 6) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 10) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 9) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 4) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 20) ;

		templatePython("teaParty", new String[]{"Int","Int"},
				"def teaParty(tea, candy):\n",
				"	if (tea < 5 or candy < 5):\n"+
				"		return 0\n"+
				"	elif (tea >= 2*candy or candy >= 2*tea):\n"+ 
				"		return 2\n"+
				"	else:\n" +
				"		return 1\n");
		templateScala("teaParty", new String[]{"Int","Int"}, 
				"def teaParty(tea:Int, candy:Int): Int = {\n",
				"	if (tea < 5 || candy < 5)\n"+
				"		return 0\n"+
				"	else if (tea >= 2*candy || candy >= 2*tea)\n"+ 
				"		return 2\n"+
				"	else\n" +
				"		return 1\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( teaParty((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int teaParty(int tea, int candy) {
		/* BEGIN SOLUTION */
		if (tea < 5 || candy < 5)
			return 0;
		else if (tea >= 2*candy || candy >= 2*tea) 
			return 2;
		else // (tea >= 5 && candy >= 5)
			return 1;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
