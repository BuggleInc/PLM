/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaMaxMod5Entity extends BatEntity {
  override def run(t: BatTest) {
    t.setResult(maxMod5(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]))
  }

  /* BEGIN TEMPLATE */
  def maxMod5(a: Int, b: Int): Int = {
    /* BEGIN SOLUTION */
    if (a == b)
      0
    else if (a > b) {
      if (a % 5 == b % 5)
        b
      else
        a
    } else if (a % 5 == b % 5)
      a
    else
      b
    /* END SOLUTION */
  }
  /* END TEMPLATE */


}
