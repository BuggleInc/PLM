package array.golomb;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

class ScalaGolombEntity extends BatEntity {

	/* BEGIN TEMPLATE */
def golomb(num:Int): Int = {
		/* BEGIN SOLUTION */
  if(num==1)
  		return 1;
  else
  		return 1+golomb(num-golomb(golomb(num-1)));
		/* END SOLUTION */
}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( golomb((int)t.getParameter(0)) );
	}
}

