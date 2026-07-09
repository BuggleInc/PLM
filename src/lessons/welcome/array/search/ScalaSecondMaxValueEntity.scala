package lessons.welcome.array.search

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaSecondMaxValueEntity extends BatEntity {

  override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(max2Value( param(0).asInstanceOf[Array[Int]] )))
      }
	}

	/* BEGIN TEMPLATE */
	def max2Value(nums:Array[Int]): Int = {
  	/* BEGIN SOLUTION */
    var max=Integer.MIN_VALUE
    var sec=Integer.MIN_VALUE
    for (i <- 0 to nums.length-1)
      if (nums(i) > max) {
        sec = max
        max = nums(i)
      } else if (nums(i) > sec) {
        sec = nums(i)
      }
    return sec
  	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
