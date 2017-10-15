/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class SortaSum extends ExerciseTemplated {
	public SortaSum(Lesson lesson, FileUtils fileUtils) {
		super("SortaSum");

		BatWorld myWorld = new BatWorld(fileUtils, "sortaSum");
		myWorld.addTest(BatTest.VISIBLE, 3, 4) ;
		myWorld.addTest(BatTest.VISIBLE, 9, 4) ;
		myWorld.addTest(BatTest.VISIBLE, 10, 11) ;
		myWorld.addTest(BatTest.INVISIBLE, 12, -3) ;
		myWorld.addTest(BatTest.INVISIBLE, -3, 12) ;
		myWorld.addTest(BatTest.INVISIBLE, 4, 5) ;
		myWorld.addTest(BatTest.INVISIBLE, 4, 6) ;
		myWorld.addTest(BatTest.INVISIBLE, 14, 7) ;
		myWorld.addTest(BatTest.INVISIBLE, 14, 6) ;

		setup(myWorld);
	}
}
