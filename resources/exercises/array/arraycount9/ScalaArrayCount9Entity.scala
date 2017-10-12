package array.arraycount9
import plm.universe.bat.{BatEntity, BatTest}

class ScalaArrayCount9Entity extends BatEntity {

	/* BEGIN TEMPLATE */
	def arrayCount9(nums:Array[Int]): Int = {
		/* BEGIN SOLUTION */
		var res = 0
		for (value <- nums)
			if (value == 9)
				res += 1
		res
		/* END SOLUTION */
	}
	/* END TEMPLATE */


	override def run(t: BatTest) {
		t.setResult( arrayCount9(t.getParameter(0).asInstanceOf[Array[Int]]) )
	}
}
