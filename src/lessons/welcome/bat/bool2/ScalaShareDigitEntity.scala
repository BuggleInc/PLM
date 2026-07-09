package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaShareDigitEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(shareDigit(param(0).asInstanceOf[Int], param(1).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def shareDigit(a:Int, b:Int):Boolean = {
	/* BEGIN SOLUTION */
   return (a/10 == b/10 || a/10 == b%10 || a%10 == b/10 || a%10 == b%10)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
