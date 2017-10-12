/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class LessBy10 extends ExerciseTemplated {
	public LessBy10(Lesson lesson) {
		super("LessBy10");

		BatWorld myWorld = new BatWorld("lessBy10");
		myWorld.addTest(BatTest.VISIBLE, 1, 7, 11) ;
		myWorld.addTest(BatTest.VISIBLE, 1, 7, 10) ;
		myWorld.addTest(BatTest.VISIBLE, 11, 1, 7) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 7, 1) ;
		myWorld.addTest(BatTest.INVISIBLE, -10, 2, 2) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 11, 11) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 3, 30) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 3, 3) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 1, 11) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 11, 1) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 11, 2) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 30, 3) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 2, -8) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 8, 12) ;

		setup(myWorld);
	}
}
