/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class FizzBuzz extends ExerciseTemplated {
	public FizzBuzz(Lesson lesson, FileUtils fileUtils) {
		super("FizzBuzz");

		BatWorld myWorld = new BatWorld(fileUtils, "fizzBuzz");
		myWorld.addTest(BatTest.VISIBLE, 2) ;
		myWorld.addTest(BatTest.VISIBLE, 3) ;
		myWorld.addTest(BatTest.VISIBLE, 4) ;
		myWorld.addTest(BatTest.VISIBLE, 5) ;
		myWorld.addTest(BatTest.VISIBLE, 6) ;
		myWorld.addTest(BatTest.VISIBLE, 7) ;
		myWorld.addTest(BatTest.VISIBLE, 8) ;
		myWorld.addTest(BatTest.VISIBLE, 9) ;
		myWorld.addTest(BatTest.INVISIBLE, 10) ;
		myWorld.addTest(BatTest.INVISIBLE, 11) ;
		myWorld.addTest(BatTest.INVISIBLE, 12) ;
		myWorld.addTest(BatTest.INVISIBLE, 15) ;
		myWorld.addTest(BatTest.INVISIBLE, 16) ;
		myWorld.addTest(BatTest.INVISIBLE, 18) ;

		setup(myWorld);
	}
}
