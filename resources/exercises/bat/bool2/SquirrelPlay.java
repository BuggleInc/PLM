/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class SquirrelPlay extends ExerciseTemplated {
	public SquirrelPlay(FileUtils fileUtils) {
		super("SquirrelPlay");

		BatWorld myWorld = new BatWorld(fileUtils, "squirrelPlay");
		myWorld.addTest(BatTest.VISIBLE, 70, false) ;
		myWorld.addTest(BatTest.VISIBLE, 95, false) ;
		myWorld.addTest(BatTest.VISIBLE, 95, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 90, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 90, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 50, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 50, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 100, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 100, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 105, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 59, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 59, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 60, false) ;

		setup(myWorld);
	}
}
