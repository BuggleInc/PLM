package lessons.welcome.array.arrayfront9

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaArrayFront9Entity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(arrayFront9(param(0).asInstanceOf[Array[Int]]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def arrayFront9(nums:Array[Int]): Boolean = {
		/* BEGIN SOLUTION */
	  for (i <- 0 to Math.min(nums.length,4)-1)
	    if (nums(i) == 9)
	      return true
	  return false
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
