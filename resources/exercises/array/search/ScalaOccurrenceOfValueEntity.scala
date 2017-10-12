package array.search

import plm.universe.bat.{BatEntity, BatTest}

class ScalaOccurrenceOfValueEntity extends BatEntity {

  /* BEGIN TEMPLATE */
  def occurrences(nums: Array[Int], lookingFor: Int): Int = {
    /* BEGIN SOLUTION */
    var count = 0
    for (i <- nums.indices)
      if (nums(i) == lookingFor)
        count += 1
    count
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(occurrences(t.getParameter(0).asInstanceOf[Array[Int]], t.getParameter(1).asInstanceOf[Int]
    ))
  }
}




