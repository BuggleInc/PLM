package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

class ScalaWithoutDoublesEntity extends BatEntity {

	/* BEGIN TEMPLATE */
def withoutDoubles(die1:Int, die2:Int, noDoubles:Boolean):Int = {
		/* BEGIN SOLUTION */
	if (noDoubles && (die1 == die2)) {
		if (die1 == 6)
			return 1 + die2
		else
			return die1 + 1 + die2
	} else
		return die1 + die2
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( withoutDoubles((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Boolean)t.getParameter(2)) );
	}

}
