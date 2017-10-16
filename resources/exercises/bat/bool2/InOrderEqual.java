/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class InOrderEqual extends ExerciseTemplated {
	public InOrderEqual(FileUtils fileUtils) {
		super("InOrderEqual");

		BatWorld myWorld = new BatWorld(fileUtils, "inOrderEqual");
		myWorld.addTest(BatTest.VISIBLE, 2, 5, 11, false) ;
		myWorld.addTest(BatTest.VISIBLE, 5, 7, 6, false) ;
		myWorld.addTest(BatTest.VISIBLE, 5, 5, 7, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 5, 7, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 5, 4, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 4, 3, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 4, 4, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 4, 3, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 4, 4, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, 5, 5, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 5, 5, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 2, 1, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, 2, 2, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 0, 1, 0, true) ;

		setup(myWorld);
	}
}
