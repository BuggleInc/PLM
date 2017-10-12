package array.search

import plm.universe.bat.{BatEntity, BatTest}

class ScalaIndexOfMaxValueEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def indexOfMaxValue(nums: Array[Int]): Int = {
    /* BEGIN SOLUTION */
    var max = nums(0)
    var maxIdx = 0
    for (i <- nums.indices)
      if (nums(i) > max) {
        max = nums(i)
        maxIdx = i
      }
    maxIdx
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(indexOfMaxValue(t.getParameter(0).asInstanceOf[Array[Int]]))
  }
}




