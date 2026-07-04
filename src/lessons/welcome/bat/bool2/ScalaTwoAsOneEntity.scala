package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaTwoAsOneEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( twoAsOne(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int], t.getParameter(2).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def twoAsOne(a:Int, b:Int, c:Int):Boolean = {
	/* BEGIN SOLUTION */
   return (a + b == c) || (a + c == b) || (b + c == a)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
