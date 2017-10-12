package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

class ScalaSquirrelPlayEntity extends BatEntity {

	/* BEGIN TEMPLATE */
def squirrelPlay(temp:Int, isSummer:Boolean):Boolean = {
		/* BEGIN SOLUTION */
   return (temp >= 60 && ((isSummer && temp <= 100) || temp <= 90))
		/* END SOLUTION */
	}
	/* END TEMPLATE */


	override def run(t: BatTest) {
		t.setResult( squirrelPlay((Integer)t.getParameter(0), (Boolean)t.getParameter(1)) );
	}
}
