package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaClose10Entity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult(close10(t.getParameter(0).asInstanceOf[Int],t.getParameter(1).asInstanceOf[Int]));
	}

	/* BEGIN TEMPLATE */
	def close10(a:Int, b:Int): Int = {
	/* BEGIN SOLUTION */
   if (Math.abs(10-a) == Math.abs(10-b))
      return 0
   else if (Math.abs(10-a) < Math.abs(10-b))
      return a
   else
      return b
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
