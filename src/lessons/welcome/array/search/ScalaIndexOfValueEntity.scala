package lessons.welcome.array.search

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaIndexOfValueEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(indexOfValue( param(0).asInstanceOf[Array[Int]], param(1).asInstanceOf[Int] ) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def indexOfValue(nums:Array[Int] ,lookingFor:Int): Int = {
		/* BEGIN SOLUTION */
	  for (i <- 0 to nums.length-1)
	    if (nums(i)==lookingFor) 
	      return i
	  return -1
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
