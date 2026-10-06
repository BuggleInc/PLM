package lessons.welcome.array.arrayfront9

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ArrayFront9Entity extends BatEntity {

    /* BEGIN REMOTE */
    override def run(): Unit = {
      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(arrayFront9(param(0).asInstanceOf[Array[Int]]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def arrayFront9(nums:Array[Int]): Boolean = {
		/* BEGIN SOLUTION */
		nums.take(4).contains(9)
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
