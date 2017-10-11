/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ShareDigit extends ExerciseTemplated {
	public ShareDigit(Lesson lesson) {
		super("ShareDigit");

		BatWorld myWorld = new BatWorld("shareDigit");
		myWorld.addTest(BatTest.VISIBLE, 12, 23) ;
		myWorld.addTest(BatTest.VISIBLE, 12, 43) ;
		myWorld.addTest(BatTest.VISIBLE, 12, 44) ;
		myWorld.addTest(BatTest.INVISIBLE, 23, 12) ;
		myWorld.addTest(BatTest.INVISIBLE, 23, 39) ;
		myWorld.addTest(BatTest.INVISIBLE, 23, 19) ;
		myWorld.addTest(BatTest.INVISIBLE, 30, 90) ;
		myWorld.addTest(BatTest.INVISIBLE, 30, 91) ;
		myWorld.addTest(BatTest.INVISIBLE, 55, 55) ;
		myWorld.addTest(BatTest.INVISIBLE, 55, 44) ;

		templatePython("shareDigit", new String[]{"Int","Int"},
				"def shareDigit(a, b):\n",
				"   return (a/10 == b/10 or a/10 == b%10 or a%10 == b/10 or a%10 == b%10)");
		templateScala("shareDigit", new String[]{"Int","Int"}, 
				"def shareDigit(a:Int, b:Int):Boolean = {\n",
				"   return (a/10 == b/10 || a/10 == b%10 || a%10 == b/10 || a%10 == b%10)\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( shareDigit((Integer)t.getParameter(0), (Integer)t.getParameter(1)) ); 
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean shareDigit(int a, int b) {
		/* BEGIN SOLUTION */
		return (a/10 == b/10 || a/10 == b%10 || a%10 == b/10 || a%10 == b%10);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
