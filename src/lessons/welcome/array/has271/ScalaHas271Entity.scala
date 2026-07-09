package lessons.welcome.array.has271

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaHas271Entity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(has271(param(0).asInstanceOf[Array[Int]]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def has271(nums:Array[Int]): Boolean = {
		/* BEGIN SOLUTION */
	  var count=0
	  for (i <- 0 to nums.length-2)
	    if ((nums(i) + 5 == nums(i+1)) && (Math.abs(nums(i+2)-nums(i)+1)<=2))
	      return true
	  return false
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
