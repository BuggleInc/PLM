package array.array123

import plm.universe.bat.{BatEntity, BatTest}

class ScalaArray123Entity extends BatEntity {
  /* BEGIN TEMPLATE */
  def array123(nums: Array[Int]): Boolean = {
    /* BEGIN SOLUTION */
    for (i <- 0 to nums.length - 3)
      if (nums(i) == 1 && nums(i + 1) == 2 && nums(i + 2) == 3)
        return true
    /* END SOLUTION */
    return false
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(array123(t.getParameter(0).asInstanceOf[Array[Int]]))
  }
}
