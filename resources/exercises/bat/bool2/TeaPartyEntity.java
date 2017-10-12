/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class TeaPartyEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( teaParty((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
	}

	/* BEGIN TEMPLATE */
	int teaParty(int tea, int candy) {
		/* BEGIN SOLUTION */
		if (tea < 5 || candy < 5)
			return 0;
		else if (tea >= 2*candy || candy >= 2*tea) 
			return 2;
		else // (tea >= 5 && candy >= 5)
			return 1;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
