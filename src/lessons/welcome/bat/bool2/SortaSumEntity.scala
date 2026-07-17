package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class SortaSumEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(sortaSum(param(0).asInstanceOf[Int], param(1).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def sortaSum(a:Int, b:Int):Int = {
		/* BEGIN SOLUTION */
		val sum = a+b
		if (sum >= 10 && sum <= 19)
			return 20
		else
			return sum
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
