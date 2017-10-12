/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class AnswerCellEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( answerCell((Boolean)t.getParameter(0), (Boolean)t.getParameter(1), (Boolean)t.getParameter(2)) );
	}

	/* BEGIN TEMPLATE */
	boolean answerCell(boolean isMorning, boolean isMom, boolean isAsleep) {
		/* BEGIN SOLUTION */
		return (! isAsleep) && ! (isMorning && ! isMom);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
