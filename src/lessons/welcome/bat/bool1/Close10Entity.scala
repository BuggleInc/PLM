package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class Close10Entity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(close10(param(0).asInstanceOf[Int],param(1).asInstanceOf[Int])))
	  }
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
