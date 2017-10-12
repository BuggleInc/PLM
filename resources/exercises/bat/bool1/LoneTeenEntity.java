package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class LoneTeenEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( loneTeen((Integer)t.getParameter(0),(Integer)t.getParameter(1)) );		
	}

	/* BEGIN TEMPLATE */
	boolean loneTeen(int a, int b) {
		/* BEGIN SOLUTION */
		boolean teenA = a>12&&a<20;
		boolean teenB = b>12&&b<20;
		return  (teenA&&!teenB) || (teenB&&!teenA);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
