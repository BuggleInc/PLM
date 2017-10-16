/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class MaxMod5 extends ExerciseTemplated {
	public MaxMod5(FileUtils fileUtils) {
		super("MaxMod5");

		BatWorld myWorld = new BatWorld(fileUtils, "maxMod5");
		myWorld.addTest(BatTest.VISIBLE, 2, 3) ;
		myWorld.addTest(BatTest.VISIBLE, 6, 2) ;
		myWorld.addTest(BatTest.VISIBLE, 3, 2) ;
		myWorld.addTest(BatTest.INVISIBLE, 8, 12) ;
		myWorld.addTest(BatTest.INVISIBLE, 7, 12) ;
		myWorld.addTest(BatTest.INVISIBLE, 11, 6) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 7) ;
		myWorld.addTest(BatTest.INVISIBLE, 7, 7) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, 1) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, 14) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, 2) ;

		setup(myWorld);
	}
}
