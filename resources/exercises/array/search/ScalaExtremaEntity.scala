package array.search

import plm.universe.bat.{BatEntity, BatTest}

class ScalaExtremaEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def extrema(nums:Array[Int]): Int = {
    /* BEGIN SOLUTION */
    var min=nums(0)
    var max=nums(0)
    if (nums.length>0){
      for (i <- 1 to nums.length-1) {
        if (nums(i)<min)
          min=nums(i)
        if (nums(i)>max)
          max=nums(i)
      }
      max-min
    } else
      0
    /* END SOLUTION */
  }
  /* END TEMPLATE */

override def run(t: BatTest) {
  t.setResult( extrema(t.getParameter(0).asInstanceOf[Array[Int]]) )
}


}

