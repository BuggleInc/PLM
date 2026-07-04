package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaLessBy10Entity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( lessBy10(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int], t.getParameter(2).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def lessBy10(a:Int, b:Int, c:Int):Boolean = {
		/* BEGIN SOLUTION */
		return ((a - b) >= 10) || ((b - a) >= 10) || ((b - c) >= 10) || ((c - b) >= 10) || ((a - c) >= 10) || ((c - a) >= 10)
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
