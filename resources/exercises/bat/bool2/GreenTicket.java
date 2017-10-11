/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class GreenTicket extends ExerciseTemplated {
	public GreenTicket(Lesson lesson) {
		super("GreenTicket");

		BatWorld myWorld = new BatWorld("greenTicket");
		myWorld.addTest(BatTest.VISIBLE, 1, 2, 3) ;
		myWorld.addTest(BatTest.VISIBLE, 2, 2, 2) ;
		myWorld.addTest(BatTest.VISIBLE, 1, 1, 2) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 1, 1) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, 2, 1) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 2, 1) ;
		myWorld.addTest(BatTest.INVISIBLE, 0, 0, 0) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 0, 0) ;
		myWorld.addTest(BatTest.INVISIBLE, 0, 9, 10) ;
		myWorld.addTest(BatTest.INVISIBLE, 0, 10, 0) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, 9, 9) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, 0, 9) ;

		templatePython("greenTicket", new String[] {"Int","Int","Int"},
				"def greenTicket(a, b, c):\n",
				"	if (a == b and b == c):\n"+
				"		return 20\n"+
				"	elif (a == b or b == c or a == c):\n"+
				"		return 10\n"+
				"	else:\n"+
				"		return 0\n");
		templateScala("greenTicket",new String[] {"Int","Int","Int"}, 
				"def greenTicket(a:Int, b:Int, c:Int):Int = {\n",
				"	if (a == b && b == c)\n"+
				"		return 20\n"+
				"	else if (a == b || b == c || a == c)\n"+
				"		return 10\n"+
				"	else\n"+
				"		return 0\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( greenTicket((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int greenTicket(int a, int b, int c) {
		/* BEGIN SOLUTION */
		if (a == b && b == c)
			return 20;
		else if (a == b || b == c || a == c)
			return 10;
		else  // (a != b && b != a && c != a)
			return 0;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
