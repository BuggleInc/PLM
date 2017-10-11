/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class NearTen extends ExerciseTemplated {
	public NearTen(Lesson lesson) {
		super("NearTen");

		BatWorld myWorld = new BatWorld("nearTen");
		myWorld.addTest(BatTest.VISIBLE, 12) ;
		myWorld.addTest(BatTest.VISIBLE, 17) ;
		myWorld.addTest(BatTest.VISIBLE, 19) ;
		myWorld.addTest(BatTest.INVISIBLE, 21) ;
		myWorld.addTest(BatTest.INVISIBLE, 6) ;
		myWorld.addTest(BatTest.INVISIBLE, 10) ;
		myWorld.addTest(BatTest.INVISIBLE, 11) ;
		myWorld.addTest(BatTest.INVISIBLE, 12) ;
		myWorld.addTest(BatTest.INVISIBLE, 13) ;
		myWorld.addTest(BatTest.INVISIBLE, 54) ;
		myWorld.addTest(BatTest.INVISIBLE, 155) ;
		myWorld.addTest(BatTest.INVISIBLE, 158) ;
		myWorld.addTest(BatTest.INVISIBLE, 3) ;
		myWorld.addTest(BatTest.INVISIBLE, 1) ;

		templatePython("nearTen", new String[]{"Int"},
				"def nearTen(num):\n",
				"  return (num % 10) <= 2 or (num % 10) >= 8\n");
		templateScala("nearTen", new String[]{"Int"}, 
				"def nearTen(num:Int):Boolean = {\n",
				"  return (num % 10) <= 2 || (num % 10) >= 8\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( nearTen((Integer)t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean nearTen(int num) {
		/* BEGIN SOLUTION */
		return (num % 10) <= 2 || (num % 10) >= 8; 
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
