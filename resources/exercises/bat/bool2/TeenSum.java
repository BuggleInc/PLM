/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class TeenSum extends ExerciseTemplated {
	public TeenSum(Lesson lesson) {
		super("TeenSum");

		BatWorld myWorld = new BatWorld("teenSum");
		myWorld.addTest(BatTest.VISIBLE, 3, 4) ;
		myWorld.addTest(BatTest.VISIBLE, 10, 13) ;
		myWorld.addTest(BatTest.VISIBLE, 13, 2) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 19) ;
		myWorld.addTest(BatTest.INVISIBLE, 13, 13) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 10) ;
		myWorld.addTest(BatTest.INVISIBLE, 6, 14) ;
		myWorld.addTest(BatTest.INVISIBLE, 15, 2) ;
		myWorld.addTest(BatTest.INVISIBLE, 19, 19) ;
		myWorld.addTest(BatTest.INVISIBLE, 19, 20) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 18) ;
		myWorld.addTest(BatTest.INVISIBLE, 12, 4) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 20) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 17) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 16) ;
		myWorld.addTest(BatTest.INVISIBLE, 6, 7) ;

		setup(myWorld);
	}
}
