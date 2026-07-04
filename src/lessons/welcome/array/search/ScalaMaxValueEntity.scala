package lessons.welcome.array.search

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaMaxValueEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( maxValue( t.getParameter(0).asInstanceOf[Array[Int]] ));
	}

	/* BEGIN TEMPLATE */
	def maxValue(nums:Array[Int]): Int = {
		/* BEGIN SOLUTION */
	  var max=nums(0)
	  for (i <- 0 to nums.length-1)
	    if (nums(i) > max)
	      max = nums(i)
	  return max
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
