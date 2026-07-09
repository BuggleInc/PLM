package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaLastDigit2Entity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(lastDigit(param(0).asInstanceOf[Int], param(1).asInstanceOf[Int], param(2).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def lastDigit(a:Int, b:Int, c:Int):Boolean = {
		/* BEGIN SOLUTION */
		val da = a % 10
		val db = b % 10
		val dc = c % 10
		return da == db || da == dc || dc == db
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
