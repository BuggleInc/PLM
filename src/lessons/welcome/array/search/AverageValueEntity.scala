package lessons.welcome.array.search

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class AverageValueEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(averageValue(param(0).asInstanceOf[Array[Int]]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def averageValue(nums:Array[Int]): Int = {
	/* BEGIN SOLUTION */
	  var total = 0
	  for (i <- 0 to nums.length -1) 
	    total += nums(i)
	  return total / nums.length
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
