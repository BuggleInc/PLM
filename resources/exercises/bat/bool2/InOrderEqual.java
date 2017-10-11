/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class InOrderEqual extends ExerciseTemplated {
	public InOrderEqual(Lesson lesson) {
		super("InOrderEqual");

		BatWorld myWorld = new BatWorld("inOrderEqual");
		myWorld.addTest(BatTest.VISIBLE, 2, 5, 11, false) ;
		myWorld.addTest(BatTest.VISIBLE, 5, 7, 6, false) ;
		myWorld.addTest(BatTest.VISIBLE, 5, 5, 7, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 5, 7, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 5, 4, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 4, 3, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 4, 4, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 4, 3, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 4, 4, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, 5, 5, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 5, 5, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 2, 1, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, 2, 2, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 0, 1, 0, true) ;

		templatePython("inOrderEqual", new String[] {"Int","Int","Int","Boolean"},
				"def inOrderEqual(a, b, c, equalOk):\n",
				"		return (equalOk and ((a <= b) and (b <= c))) or (a < b and b < c)");
		templateScala("inOrderEqual",new String[] {"Int","Int","Int","Boolean"}, 
				"def inOrderEqual(a:Int, b:Int, c:Int, equalOk:Boolean):Boolean = {\n",
				"		return (equalOk && ((a <= b) && (b <= c))) || (a < b && b < c)\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( inOrderEqual((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2), (Boolean)t.getParameter(3)) ); 
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean inOrderEqual(int a, int b, int c, boolean equalOk) {
		/* BEGIN SOLUTION */
		return (equalOk && ((a <= b) && (b <= c))) || (a < b && b < c);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
