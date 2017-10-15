/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class TwoAsOne extends ExerciseTemplated {
	public TwoAsOne(Lesson lesson, FileUtils fileUtils) {
		super("TwoAsOne");

		BatWorld myWorld = new BatWorld(fileUtils, "twoAsOne");
		myWorld.addTest(BatTest.VISIBLE, 1, 2, 3) ;
		myWorld.addTest(BatTest.VISIBLE, 3, 1, 2) ;
		myWorld.addTest(BatTest.VISIBLE, 3, 2, 2) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 3, 1) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 3, -2) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 3, -3) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 5, 3) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, 5, 5) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, 4, 5) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 4, 9) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 3, 0) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 3, 2) ;

		setup(myWorld);
	}
}
