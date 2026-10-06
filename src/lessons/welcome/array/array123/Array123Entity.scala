package lessons.welcome.array.array123

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class Array123Entity extends BatEntity {

    /* BEGIN REMOTE */
    override def run(): Unit = {
      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(array123(param(0).asInstanceOf[Array[Int]]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def array123(nums:Array[Int]): Boolean = {
		/* BEGIN SOLUTION */
		(0 to nums.length-3).exists(i => nums(i)==1 && nums(i+1)==2 && nums(i+2)==3)
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
