/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class InOrderEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( inOrder((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2), (Boolean)t.getParameter(3)) );
	}

	/* BEGIN TEMPLATE */
	boolean inOrder(int a, int b, int c, boolean bOk) {
		/* BEGIN SOLUTION */
		return (bOk || (b > a)) && (c > b);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
