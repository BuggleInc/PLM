/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatEntity;

class ScalaFizzBuzzEntity extends BatEntity {
	/* BEGIN TEMPLATE */
def fizzBuzz(a:Int):String = {
		/* BEGIN SOLUTION */
	(a%5, a%3) match {
	    case (0,0) => return "Fizz Buzz"
	    case (_,0) => return "Fizz"
 	    case (0,_) => return "Buzz"
	    case _     => return ""+a
    }
		/* END SOLUTION */
	}
	/* END TEMPLATE */

		setup(myWorld);
	}

	override def run(t: BatTest) {
		t.setResult( fizzBuzz((Integer)t.getParameter(0)) );
	}
}
