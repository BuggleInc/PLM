package lessons.welcome.array.notriples

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaNoTriplesEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult(noTriples(t.getParameter(0).asInstanceOf[Array[Int]]));
	}

	/* BEGIN TEMPLATE */
	def noTriples(nums:Array[Int]): Boolean = {
		/* BEGIN SOLUTION */
	  var count=0
	  for (i <- 0 to nums.length-3)
	    if ( (nums(i) == nums(i+1)) && (nums(i+1) == nums(i+2)) )
	      return false
	  return true
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
