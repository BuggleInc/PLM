package bat.bool1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class CountTeenEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( countTeen((Integer)t.getParameter(0),(Integer)t.getParameter(1),(Integer)t.getParameter(2),(Integer)t.getParameter(3)) );		
	}

	/* BEGIN TEMPLATE */
	int countTeen(int a, int b, int c,int d) {
		/* BEGIN SOLUTION */
		int ret=0;
		if (a>12&&a<20)
			ret+=1;
		if (b>12&&b<20)
			ret+=1;
		if (c>12&&c<20)
			ret+=1;
		if (d>12&&d<20)
			ret+=1;
		return ret; 
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
