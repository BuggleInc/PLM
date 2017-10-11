/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class In1To10 extends ExerciseTemplated {
	public In1To10(Lesson lesson) {
		super("In1To10");

		BatWorld myWorld = new BatWorld("in1To10");
		myWorld.addTest(BatTest.VISIBLE, 5, false) ;
		myWorld.addTest(BatTest.VISIBLE, 11, false) ;
		myWorld.addTest(BatTest.VISIBLE, 11, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 0, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 0, true) ;
		myWorld.addTest(BatTest.INVISIBLE, -1, false) ;

		templatePython("in1To10", new String[]{"Int","Boolean"},
				"def in1To10(n, outsideMode):\n",
				"   return (outsideMode and (n <= 1 or n >= 10)) or ((not outsideMode) and (n >= 1 and n <= 10))\n");
		templateScala("in1To10",new String[]{"Int","Boolean"}, 
				"def in1To10(n:Int, outsideMode:Boolean):Boolean = {\n",
				"   return (outsideMode && (n <= 1 || n >= 10)) || ((! outsideMode) && (n >= 1 && n <= 10))\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( in1To10((Integer)t.getParameter(0), (Boolean)t.getParameter(1)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean in1To10(int n, boolean outsideMode) {
		/* BEGIN SOLUTION */
		return (outsideMode && (n <= 1 || n >= 10)) || ((! outsideMode) && (n >= 1 && n <= 10));
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
