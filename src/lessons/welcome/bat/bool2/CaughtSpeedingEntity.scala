package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class CaughtSpeedingEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(caughtSpeeding(param(0).asInstanceOf[Int], param(1).asInstanceOf[Boolean]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def caughtSpeeding(speed:Int, isBirthday:Boolean):Int = {
		/* BEGIN SOLUTION */
		if ((isBirthday && speed <= 65) || (speed <= 60))
			return 0
		else if ((isBirthday && speed <= 85) || (speed <= 80))
			return 1
		else
			return 2
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
