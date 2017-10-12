/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2
import plm.universe.bat.{BatEntity, BatTest}

class ScalaShareDigitEntity extends BatEntity {
	/* BEGIN TEMPLATE */
	def shareDigit(a:Int, b:Int):Boolean = {
		/* BEGIN SOLUTION */
		a / 10 == b / 10 || a / 10 == b % 10 || a % 10 == b / 10 || a % 10 == b % 10
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( shareDigit(t.getParameter(0).asInstanceOf[Int],t.getParameter(1).asInstanceOf[Int]) )
	}

}
