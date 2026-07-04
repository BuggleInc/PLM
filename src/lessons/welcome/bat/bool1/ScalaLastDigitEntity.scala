package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaLastDigitEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( lastDigit(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def lastDigit(a:Int, b:Int):Boolean = {
	/* BEGIN SOLUTION */
   return a%10 == b%10
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
