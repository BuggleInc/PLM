package lessons.welcome.array.array667

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class Array667Entity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(array667(param(0).asInstanceOf[Array[Int]]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def array667(nums:Array[Int]): Int = {
		/* BEGIN SOLUTION */
	  var count=0
	  for (i <- 0 to nums.length-2)
	    if ((nums(i) == 6) && (nums(i+1)==6 || nums(i+1)==7))
	      count += 1
	  return count
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
