package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class TeenSumEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(teenSum(param(0).asInstanceOf[Int], param(1).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def teenSum(a:Int, b:Int):Int = {
	/* BEGIN SOLUTION */
	if ((a >= 13 && a <= 19) || (b >= 13 && b <= 19))
		return 19
	else
		return a+b
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
