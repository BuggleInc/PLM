/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class WithoutDoubles extends ExerciseTemplated {
	public WithoutDoubles(FileUtils fileUtils) {
		super("WithoutDoubles");

		BatWorld myWorld = new BatWorld(fileUtils, "withoutDoubles");
		myWorld.addTest(BatTest.VISIBLE, 2, 3, true) ;
		myWorld.addTest(BatTest.VISIBLE, 3, 3, true) ;
		myWorld.addTest(BatTest.VISIBLE, 3, 3, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 3, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 4, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 4, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 5, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 5, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 6, 6, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 6, 6, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, 6, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 6, 1, false) ;

		setup(myWorld);
	}
}
