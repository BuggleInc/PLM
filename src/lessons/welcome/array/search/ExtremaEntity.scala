package lessons.welcome.array.search

import plm.universe.bat.BatEntity 
import plm.universe.bat.BatTest

class ExtremaEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(extrema(param(0).asInstanceOf[Array[Int]])))
	} }

  /* BEGIN TEMPLATE */
  def extrema(nums : Array[Int]) : Int = {
        /* BEGIN SOLUTION */
        if (nums.length > 0){
			var min = nums(0) 
			var max = nums(0) 
			for (i <- 1 to nums.length - 1) {
				if (nums(i) < min) 
					min = nums(i) 
				if (nums(i) > max) 
					max = nums(i)
			} 
			return max - min
		} else return 0
        /* END SOLUTION */
      }
  /* END TEMPLATE */
}
