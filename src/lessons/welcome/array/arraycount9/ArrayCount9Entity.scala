package lessons.welcome.array.arraycount9

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ArrayCount9Entity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(arrayCount9(param(0).asInstanceOf[Array[Int]]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def arrayCount9(nums:Array[Int]): Int = {
		/* BEGIN SOLUTION */
	  var res = 0
	  for (value <- nums)
	    if (value == 9)
	      res += 1
	  return res
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
