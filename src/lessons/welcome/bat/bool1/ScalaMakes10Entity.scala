package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaMakes10Entity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( makes10(t.getParameter(0).asInstanceOf[Int],t.getParameter(1).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def makes10(a:Int, b:Int):Boolean = {
	/* BEGIN SOLUTION */
   return (a==10) || (b==10) || ((a+b)==10)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
