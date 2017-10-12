/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class AlarmClock extends ExerciseTemplated {
	public AlarmClock(Lesson lesson) {
		super("AlarmClock");

		BatWorld myWorld = new BatWorld("alarmClock");
		myWorld.addTest(BatTest.VISIBLE, 1, false) ;
		myWorld.addTest(BatTest.VISIBLE, 5, false) ;
		myWorld.addTest(BatTest.VISIBLE, 0, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 6, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 0, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 6, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 1, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, true) ;

		setup(myWorld);
	}
}
