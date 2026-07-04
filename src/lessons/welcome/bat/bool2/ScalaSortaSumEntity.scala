package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaSortaSumEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( sortaSum(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def sortaSum(a:Int, b:Int):Int = {
		/* BEGIN SOLUTION */
		val sum = a+b
		if (sum >= 10 && sum <= 19)
			return 20
		else
			return sum
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
