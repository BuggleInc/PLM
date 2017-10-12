/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaSortaSumEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def sortaSum(a: Int, b: Int): Int = {
    /* BEGIN SOLUTION */
    val sum = a + b
    if (sum >= 10 && sum <= 19)
      20
    else
      sum
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(sortaSum(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]) )
  }
}
