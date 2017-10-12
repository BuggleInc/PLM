package array.search

import plm.universe.bat.{BatEntity, BatTest}

class ScalaSecondMaxValueEntity extends BatEntity {

  /* BEGIN TEMPLATE */
  def max2Value(nums: Array[Int]): Int = {
    /* BEGIN SOLUTION */
    var max = Integer.MIN_VALUE
    var sec = Integer.MIN_VALUE
    for (i <- nums.indices)
      if (nums(i) > max) {
        sec = max
        max = nums(i)
      } else if (nums(i) > sec) {
        sec = nums(i)
      }
    sec
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(max2Value(t.getParameter(0).asInstanceOf[Array[Int]]))
  }

}




