package lessons.welcome.array.search

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class IndexOfMaxValueEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(indexOfMaxValue( param(0).asInstanceOf[Array[Int]]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def indexOfMaxValue(nums:Array[Int]):Int = {
		/* BEGIN SOLUTION */
	  var max=nums(0)
	  var maxIdx = 0
	  for (i <- 0 to nums.length-1)
	    if (nums(i)>max) {
	      max = nums(i)
	      maxIdx = i
	    }
	  return maxIdx
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
