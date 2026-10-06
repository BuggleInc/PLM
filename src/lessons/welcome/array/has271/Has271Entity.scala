package lessons.welcome.array.has271

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class Has271Entity extends BatEntity {

    /* BEGIN REMOTE */
    override def run(): Unit = {
      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(has271(param(0).asInstanceOf[Array[Int]]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def has271(nums:Array[Int]): Boolean = {
		/* BEGIN SOLUTION */
		(0 to nums.length-2).exists(i => nums(i) + 5 == nums(i+1) && Math.abs(nums(i+2)-nums(i)+1) <= 2)
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
