package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaIn1To10Entity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(in1To10(param(0).asInstanceOf[Int], param(1).asInstanceOf[Boolean]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def in1To10(n:Int, outsideMode:Boolean):Boolean = {
		/* BEGIN SOLUTION */
   		return (outsideMode && (n <= 1 || n >= 10)) || ((! outsideMode) && (n >= 1 && n <= 10))
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
