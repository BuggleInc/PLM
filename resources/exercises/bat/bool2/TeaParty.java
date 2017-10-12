/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class TeaParty extends ExerciseTemplated {
	public TeaParty(Lesson lesson) {
		super("TeaParty");

		BatWorld myWorld = new BatWorld("teaParty");
		myWorld.addTest(BatTest.VISIBLE, 6, 8) ;
		myWorld.addTest(BatTest.VISIBLE, 3, 8) ;
		myWorld.addTest(BatTest.VISIBLE, 20, 6) ;
		myWorld.addTest(BatTest.INVISIBLE, 12, 6) ;
		myWorld.addTest(BatTest.INVISIBLE, 11, 6) ;
		myWorld.addTest(BatTest.INVISIBLE, 11, 4) ;
		myWorld.addTest(BatTest.INVISIBLE, 4, 5) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 5) ;
		myWorld.addTest(BatTest.INVISIBLE, 6, 6) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 10) ;
		myWorld.addTest(BatTest.INVISIBLE, 5, 9) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 4) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 20) ;
		setup(myWorld);
	}
}
