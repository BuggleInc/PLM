package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaParotTroubleEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( parotTrouble(t.getParameter(0).asInstanceOf[Boolean],t.getParameter(1).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def parotTrouble(talking:Boolean, hour:Int):Boolean = {
	/* BEGIN SOLUTION */
  return (talking && (hour<7||hour>20))
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
