package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaTeenSumEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( teenSum(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def teenSum(a:Int, b:Int):Int = {
	/* BEGIN SOLUTION */
	if ((a >= 13 && a <= 19) || (b >= 13 && b <= 19))
		return 19
	else
		return a+b
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
