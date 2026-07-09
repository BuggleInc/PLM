package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaNearTenEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(nearTen(param(0).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def nearTen(num:Int):Boolean = {
	/* BEGIN SOLUTION */
  return (num % 10) <= 2 || (num % 10) >= 8
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
