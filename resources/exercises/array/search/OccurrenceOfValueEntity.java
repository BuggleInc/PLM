package array.search;

import java.util.Random;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class OccurrenceOfValueEntity extends BatEntity {
	/* BEGIN TEMPLATE */
	// counts the occurrences of the value 'lookingFor' contained in tab variable
	public int occurrences(int[] tab, int lookingFor) {
		/* BEGIN SOLUTION */
		int count = 0;
		for (int i=0; i<tab.length; i++) {
			if (tab[i] == lookingFor) {
				count++;
			}
		}
		return count;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
   
	public void run(BatTest t) {
		t.setResult( occurrences( (int[])t.getParameter(0), (Integer)t.getParameter(1) ) );
	}
}





