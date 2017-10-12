/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class FizzBuzzEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( fizzBuzz((Integer)t.getParameter(0)) );
	}

	/* BEGIN TEMPLATE */
	String fizzBuzz(int a) {
		/* BEGIN SOLUTION */
		if (a%5 == 0 && a%3 ==0) 
			return "Fizz Buzz";
		else if (a%5 ==0)
			return "Buzz";
		else if (a%3 == 0)
			return "Fizz";
		return ""+a;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
