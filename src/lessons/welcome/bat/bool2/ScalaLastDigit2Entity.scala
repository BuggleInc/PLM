package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaLastDigit2Entity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( lastDigit(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int], t.getParameter(2).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def lastDigit(a:Int, b:Int, c:Int):Boolean = {
		/* BEGIN SOLUTION */
		val da = a % 10
		val db = b % 10
		val dc = c % 10
		return da == db || da == dc || dc == db
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
