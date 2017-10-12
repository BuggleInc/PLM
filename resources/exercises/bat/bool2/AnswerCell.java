/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class AnswerCell extends ExerciseTemplated {
	public AnswerCell(Lesson lesson) {
		super("AnswerCell");

		BatWorld myWorld = new BatWorld("answerCell");
		myWorld.addTest(BatTest.VISIBLE, false, false, false) ;
		myWorld.addTest(BatTest.VISIBLE, false, false, true) ;
		myWorld.addTest(BatTest.VISIBLE, true, false, false) ;
		myWorld.addTest(BatTest.INVISIBLE, true, true, false) ;
		myWorld.addTest(BatTest.INVISIBLE, false, true, false) ;
		myWorld.addTest(BatTest.INVISIBLE, true, true, true) ;

		setup(myWorld);
	}
}
