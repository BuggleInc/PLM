/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class DateFashion extends ExerciseTemplated {
	public DateFashion(Lesson lesson) {
		super("DateFashion");

		BatWorld myWorld = new BatWorld("dateFashion");
		myWorld.addTest(BatTest.VISIBLE, 5, 10) ;
		myWorld.addTest(BatTest.VISIBLE, 5, 2) ;
		myWorld.addTest(BatTest.VISIBLE, 5, 5) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 3) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 2) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 9) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, 9) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 5) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 2) ;
		myWorld.addTest(BatTest.INVISIBLE, 3, 7) ;
		myWorld.addTest(BatTest.INVISIBLE, 2, 7) ;
		myWorld.addTest(BatTest.INVISIBLE, 6, 2) ;

		setup(myWorld);
	}
}
