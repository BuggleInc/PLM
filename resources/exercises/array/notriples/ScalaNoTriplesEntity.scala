package array.notriples

import plm.universe.bat.{BatEntity, BatTest}

class ScalaNoTriplesEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def noTriples(nums: Array[Int]): Boolean = {
    /* BEGIN SOLUTION */
    for (i <- 0 to nums.length - 3)
      if ((nums(i) == nums(i + 1)) && (nums(i + 1) == nums(i + 2)))
        return false
    return true
    /* END SOLUTION */
  }

  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(noTriples(t.getParameter(0).asInstanceOf[Array[Int]]))
  }
}
