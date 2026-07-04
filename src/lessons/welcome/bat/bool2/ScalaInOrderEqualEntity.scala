package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaInOrderEqualEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( inOrderEqual(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int], t.getParameter(2).asInstanceOf[Int], t.getParameter(3).asInstanceOf[Boolean]) );
	}

	/* BEGIN TEMPLATE */
	def inOrderEqual(a:Int, b:Int, c:Int, equalOk:Boolean):Boolean = {
		/* BEGIN SOLUTION */
			return (equalOk && ((a <= b) && (b <= c))) || (a < b && b < c)
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
