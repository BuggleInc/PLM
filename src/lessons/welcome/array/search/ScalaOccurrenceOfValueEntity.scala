package lessons.welcome.array.search

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaOccurrenceOfValueEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(occurrences( param(0).asInstanceOf[Array[Int]], param(1).asInstanceOf[Int] ) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def occurrences(nums:Array[Int],lookingFor:Int):Int = {
		/* BEGIN SOLUTION */
	  var count = 0
	  for (i <- 0 to nums.length-1)
	    if (nums(i) == lookingFor)
	      count += 1
	  return count
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
