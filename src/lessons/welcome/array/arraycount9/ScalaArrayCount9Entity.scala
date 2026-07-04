package lessons.welcome.array.arraycount9

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaArrayCount9Entity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( arrayCount9(t.getParameter(0).asInstanceOf[Array[Int]]) );
	}

	/* BEGIN TEMPLATE */
	def arrayCount9(nums:Array[Int]): Int = {
		/* BEGIN SOLUTION */
	  var res = 0
	  for (value <- nums)
	    if (value == 9)
	      res += 1
	  return res
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
