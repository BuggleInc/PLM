/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaTwoAsOneEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def twoAsOne(a: Int, b: Int, c: Int): Boolean = {
    /* BEGIN SOLUTION */
    (a + b == c) || (a + c == b) || (b + c == a)
    /* END SOLUTION */
  }

  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(twoAsOne(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int], t.getParameter(2).asInstanceOf[Int]))
  }
}
