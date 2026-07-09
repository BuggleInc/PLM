package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaMaxMod5Entity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(maxMod5(param(0).asInstanceOf[Int], param(1).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def maxMod5(a:Int, b:Int):Int = {
		/* BEGIN SOLUTION */
		if (a == b)
			return 0
		else if (a > b) {
			if (a % 5 == b % 5)
				return b
			else
				return a
		} else
			if (a % 5 == b % 5)
				return a
			else
				return b
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
