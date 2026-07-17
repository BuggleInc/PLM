package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class SquirrelPlayEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(squirrelPlay(param(0).asInstanceOf[Int], param(1).asInstanceOf[Boolean]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def squirrelPlay(temp:Int, isSummer:Boolean):Boolean = {
		/* BEGIN SOLUTION */
	   return (temp >= 60 && ((isSummer && temp <= 100) || temp <= 90))
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
