/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class BlueTicket extends ExerciseTemplated {
	public BlueTicket(Lesson lesson) {
		super("BlueTicket");

		BatWorld myWorld = new BatWorld("blueTicket");
		myWorld.addTest(BatTest.VISIBLE, 9, 1, 0) ;
		myWorld.addTest(BatTest.VISIBLE, 9, 2, 0) ;
		myWorld.addTest(BatTest.VISIBLE, 6, 1, 4) ;
		myWorld.addTest(BatTest.INVISIBLE, 6, 1, 5) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 0, 0) ;
		myWorld.addTest(BatTest.INVISIBLE, 15, 0, 5) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 15, 5) ;
		myWorld.addTest(BatTest.INVISIBLE, 4, 11, 1) ;
		myWorld.addTest(BatTest.INVISIBLE, 13, 2, 3) ;
		myWorld.addTest(BatTest.INVISIBLE, 8, 4, 3) ;
		myWorld.addTest(BatTest.INVISIBLE, 8, 4, 2) ;
		myWorld.addTest(BatTest.INVISIBLE, 8, 4, 1) ;

		setup(myWorld);
	}
}
