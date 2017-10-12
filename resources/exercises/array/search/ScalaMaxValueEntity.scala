package array.search

import plm.universe.bat.{BatEntity, BatTest}

class ScalaMaxValueEntity extends BatEntity {

  /* BEGIN TEMPLATE */
  def maxValue(nums:Array[Int]): Int = {
    /* BEGIN SOLUTION */
    var max=nums(0)
    for (i <- nums.indices)
      if (nums(i) > max)
        max = nums(i)
    max
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult( maxValue( t.getParameter(0).asInstanceOf[Array[Int]] ))
  }
}




