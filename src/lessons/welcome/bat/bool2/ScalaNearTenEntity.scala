package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaNearTenEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( nearTen(t.getParameter(0).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def nearTen(num:Int):Boolean = {
	/* BEGIN SOLUTION */
  return (num % 10) <= 2 || (num % 10) >= 8
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
