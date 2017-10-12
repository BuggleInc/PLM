/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class GreenTicket extends ExerciseTemplated {
	public GreenTicket(Lesson lesson) {
		super("GreenTicket");

		BatWorld myWorld = new BatWorld("greenTicket");
		myWorld.addTest(BatTest.VISIBLE, 1, 2, 3) ;
		myWorld.addTest(BatTest.VISIBLE, 2, 2, 2) ;
		myWorld.addTest(BatTest.VISIBLE, 1, 1, 2) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 1, 1) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, 2, 1) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 2, 1) ;
		myWorld.addTest(BatTest.INVISIBLE, 0, 0, 0) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 0, 0) ;
		myWorld.addTest(BatTest.INVISIBLE, 0, 9, 10) ;
		myWorld.addTest(BatTest.INVISIBLE, 0, 10, 0) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, 9, 9) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, 0, 9) ;

		setup(myWorld);
	}
}
