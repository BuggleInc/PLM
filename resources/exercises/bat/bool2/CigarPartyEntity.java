/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class CigarPartyEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( cigarParty((Integer)t.getParameter(0), (Boolean)t.getParameter(1)) );
	}

	/* BEGIN TEMPLATE */
	boolean cigarParty(int cigars, boolean isWeekend) {
		/* BEGIN SOLUTION */
		return (isWeekend && cigars >= 40) || (!isWeekend && (cigars >= 40) && (cigars <= 60));
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
