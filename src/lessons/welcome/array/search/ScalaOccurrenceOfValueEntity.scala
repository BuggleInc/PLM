package lessons.welcome.array.search

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaOccurrenceOfValueEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( occurrences( t.getParameter(0).asInstanceOf[Array[Int]], t.getParameter(1).asInstanceOf[Int] ) );
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
