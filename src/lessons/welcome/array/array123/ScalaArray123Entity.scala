package lessons.welcome.array.array123

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaArray123Entity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( array123(t.getParameter(0).asInstanceOf[Array[Int]]) );
	}

	/* BEGIN TEMPLATE */
	def array123(nums:Array[Int]): Boolean = {
		/* BEGIN SOLUTION */
	  for (i <- 0 to nums.length-3)
	    if (nums(i)==1  &&  nums(i+1)==2  &&  nums(i+2)==3)
	      return true
	  return false
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
