/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class RedTicket extends ExerciseTemplated {
	public RedTicket(Lesson lesson, FileUtils fileUtils) {
		super("RedTicket");

		BatWorld myWorld = new BatWorld(fileUtils, "redTicket");
		myWorld.addTest(BatTest.VISIBLE, 2, 2, 2) ;
		myWorld.addTest(BatTest.VISIBLE, 2, 2, 1) ;
		myWorld.addTest(BatTest.VISIBLE, 0, 0, 0) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 0, 0) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, 1, 1) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, 2, 1) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, 2, 0) ;
		myWorld.addTest(BatTest.INVISIBLE, 0, 2, 2) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, 2, 2) ;
		myWorld.addTest(BatTest.INVISIBLE, 0, 2, 0) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, 1, 2) ;

		setup(myWorld);
	}
}
