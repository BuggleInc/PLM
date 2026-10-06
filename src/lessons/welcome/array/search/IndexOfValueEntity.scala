package lessons.welcome.array.search

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class IndexOfValueEntity extends BatEntity {

    /* BEGIN REMOTE */
    override def run(): Unit = {
      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(indexOfValue( param(0).asInstanceOf[Array[Int]], param(1).asInstanceOf[Int] ) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def indexOfValue(nums:Array[Int] ,lookingFor:Int): Int = {
		/* BEGIN SOLUTION */
		nums.indexOf(lookingFor)
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
