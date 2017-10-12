package array.search

import plm.universe.bat.{BatEntity, BatTest}

class ScalaAverageValueEntity extends BatEntity {

  /* BEGIN TEMPLATE */
  def averageValue(nums: Array[Int]): Int = {
    /* BEGIN SOLUTION */
    var total = 0
    for (i <- 0 to nums.length - 1)
      total += nums(i)
    return total / nums.length
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(averageValue(t.getParameter(0).asInstanceOf[Array[Int]]));
  }
}
