/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2
import plm.universe.bat.{BatEntity, BatTest}

class ScalaTeenSumEntity extends BatEntity {
	/* BEGIN TEMPLATE */
	def teenSum(a:Int, b:Int):Int = {
		/* BEGIN SOLUTION */
		if ((a >= 13 && a <= 19) || (b >= 13 && b <= 19))
			19
		else
			a+b
		/* END SOLUTION */
	}
	/* END TEMPLATE */


	override def run(t: BatTest) {
		t.setResult( teenSum(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]) )
	}
}
