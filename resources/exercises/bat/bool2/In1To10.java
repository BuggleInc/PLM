/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class In1To10 extends ExerciseTemplated {
	public In1To10(Lesson lesson, FileUtils fileUtils) {
		super("In1To10");

		BatWorld myWorld = new BatWorld(fileUtils, "in1To10");
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

		setup(myWorld);
	}
}
