/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class CigarParty extends ExerciseTemplated {
	public CigarParty(Lesson lesson) {
		super("CigarParty");

		BatWorld myWorld = new BatWorld("cigarParty");
		myWorld.addTest(BatTest.VISIBLE, 30, false) ;
		myWorld.addTest(BatTest.VISIBLE, 50, false) ;
		myWorld.addTest(BatTest.VISIBLE, 70, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 30, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 50, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 60, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 61, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 40, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 39, false) ;
		myWorld.addTest(BatTest.INVISIBLE, 40, true) ;
		myWorld.addTest(BatTest.INVISIBLE, 39, true) ;

		setup(myWorld);
	}
}
