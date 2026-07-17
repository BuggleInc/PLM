package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class SleepInEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(sleepIn(param(0).asInstanceOf[Boolean],param(1).asInstanceOf[Boolean]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def sleepIn(weekday:Boolean, vacation:Boolean): Boolean = {
	/* BEGIN SOLUTION */
  return !weekday || vacation;
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
