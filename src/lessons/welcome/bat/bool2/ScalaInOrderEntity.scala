package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaInOrderEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( inOrder(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int], t.getParameter(2).asInstanceOf[Int], t.getParameter(3).asInstanceOf[Boolean]) );
	}

	/* BEGIN TEMPLATE */
	def inOrder(a:Int, b:Int, c:Int, bOk:Boolean):Boolean = {
	/* BEGIN SOLUTION */
		return (bOk || (b > a)) && (c > b)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
