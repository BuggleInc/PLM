package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaIn1020Entity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( in1020(t.getParameter(0).asInstanceOf[Int],t.getParameter(1).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def in1020(a:Int, b:Int):Boolean = {
	/* BEGIN SOLUTION */
   return (a>9 && a<21) || (b>9 && b<21)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
