package array.arrayfront9

import plm.universe.bat.{BatEntity, BatTest}

class ScalaArrayFront9Entity extends BatEntity {

  /* BEGIN TEMPLATE */
  def arrayFront9(nums: Array[Int]): Boolean = {
    /* BEGIN SOLUTION */
    for (i <- 0 to Math.min(nums.length, 4) - 1)
      if (nums(i) == 9)
        return true
    return false
    /* END SOLUTION */
  }

  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(arrayFront9(t.getParameter(0).asInstanceOf[Array[Int]]))
  }
}
