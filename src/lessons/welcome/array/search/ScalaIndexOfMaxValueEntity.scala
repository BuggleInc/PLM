package lessons.welcome.array.search

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaIndexOfMaxValueEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( indexOfMaxValue( t.getParameter(0).asInstanceOf[Array[Int]]) );
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
