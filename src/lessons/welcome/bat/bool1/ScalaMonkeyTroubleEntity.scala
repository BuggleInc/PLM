package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaMonkeyTroubleEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(monkeyTrouble(param(0).asInstanceOf[Boolean],param(1).asInstanceOf[Boolean]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def monkeyTrouble(aSmile:Boolean, bSmile:Boolean): Boolean = {
	/* BEGIN SOLUTION */
   return ((aSmile && bSmile) || (!aSmile && !bSmile))
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
