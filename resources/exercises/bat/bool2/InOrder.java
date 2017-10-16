/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class InOrder extends ExerciseTemplated {
	public InOrder(FileUtils fileUtils) {
		super("InOrder");

		BatWorld myWorld = new BatWorld(fileUtils, "inOrder");
		myWorld.addTest(BatTest.VISIBLE, 1, 2, 4, false) ;
		myWorld.addTest(BatTest.VISIBLE, 1, 2, 1, false) ;
		myWorld.addTest(BatTest.VISIBLE, 1, 1, 2, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 2, 4, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 3, 4, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 2, 4, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 4, 2, 2, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 4, 5, 2, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 4, 6, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 7, 9, 10, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 7, 5, 6, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 7, 5, 4, true) ;

		setup(myWorld);
	}
}
