package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaIcyHotEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( icyHot(t.getParameter(0).asInstanceOf[Int],t.getParameter(1).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def icyHot(temp1:Int, temp2:Int):Boolean = {
	/* BEGIN SOLUTION */
   return temp1<0 && temp2>100 || temp1>100 && temp2<0
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
