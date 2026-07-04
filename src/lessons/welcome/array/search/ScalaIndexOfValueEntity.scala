package lessons.welcome.array.search

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaIndexOfValueEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( indexOfValue( t.getParameter(0).asInstanceOf[Array[Int]], t.getParameter(1).asInstanceOf[Int] ) );
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
