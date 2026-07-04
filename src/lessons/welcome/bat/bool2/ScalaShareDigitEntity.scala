package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaShareDigitEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( shareDigit(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def shareDigit(a:Int, b:Int):Boolean = {
	/* BEGIN SOLUTION */
   return (a/10 == b/10 || a/10 == b%10 || a%10 == b/10 || a%10 == b%10)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
