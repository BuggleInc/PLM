/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class LastDigit2 extends ExerciseTemplated {
	public LastDigit2(Lesson lesson, FileUtils fileUtils) {
		super("LastDigit2");

		BatWorld myWorld = new BatWorld(fileUtils, "lastDigit");
		myWorld.addTest(BatTest.VISIBLE, 23, 19, 13) ;
		myWorld.addTest(BatTest.VISIBLE, 23, 19, 12) ;
		myWorld.addTest(BatTest.VISIBLE, 23, 19, 3) ;
		myWorld.addTest(BatTest.INVISIBLE, 23, 19, 39) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, 2, 3) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, 1, 2) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, 2, 2) ;
		myWorld.addTest(BatTest.INVISIBLE, 14, 25, 43) ;
		myWorld.addTest(BatTest.INVISIBLE, 14, 25, 45) ;
		myWorld.addTest(BatTest.INVISIBLE, 248, 106, 1002) ;
		myWorld.addTest(BatTest.INVISIBLE, 248, 106, 1008) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 11, 20) ;
		myWorld.addTest(BatTest.INVISIBLE, 0, 11, 0) ;

		setup(myWorld);
	}
}
