/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ShareDigit extends ExerciseTemplated {
	public ShareDigit(FileUtils fileUtils) {
		super("ShareDigit");

		BatWorld myWorld = new BatWorld(fileUtils, "shareDigit");
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

		setup(myWorld);
	}
}
