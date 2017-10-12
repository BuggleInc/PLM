package array.search

import plm.universe.bat.{BatEntity, BatTest}

class ScalaIndexOfValueEntity extends BatEntity {

  /* BEGIN TEMPLATE */
  def indexOfValue(nums: Array[Int], lookingFor: Int): Int = {
    /* BEGIN SOLUTION */
    for (i <- nums.indices)
      if (nums(i) == lookingFor)
        return i
    -1
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(indexOfValue(t.getParameter(0).asInstanceOf[Array[Int]], t.getParameter(1).asInstanceOf[Int]))
  }
}




