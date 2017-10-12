package array.has271

import plm.universe.bat.{BatEntity, BatTest}

class ScalaHas271Entity extends BatEntity {

  /* BEGIN TEMPLATE */
  def has271(nums: Array[Int]): Boolean = {
    /* BEGIN SOLUTION */
    var count = 0
    for (i <- 0 to nums.length - 2)
      if ((nums(i) + 5 == nums(i + 1)) && (Math.abs(nums(i + 2) - nums(i) + 1) <= 2))
        return true
    return false
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(has271(t.getParameter(0).asInstanceOf[Array[Int]]))
  }
}
