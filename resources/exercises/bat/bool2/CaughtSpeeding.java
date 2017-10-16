/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class CaughtSpeeding extends ExerciseTemplated {
	public CaughtSpeeding(Lesson lesson, FileUtils fileUtils) {
		super("CaughtSpeeding");

		BatWorld myWorld = new BatWorld(fileUtils, "caughtSpeeding");
		myWorld.addTest(BatTest.VISIBLE, 60, false) ;
		myWorld.addTest(BatTest.VISIBLE, 65, false) ;
		myWorld.addTest(BatTest.VISIBLE, 65, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 80, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 85, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 85, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 70, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 75, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 75, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 40, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 40, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 90, false) ;

		setup(myWorld);
	}
}
