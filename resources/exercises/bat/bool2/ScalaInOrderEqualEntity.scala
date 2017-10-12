/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaInOrderEqualEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def inOrderEqual(a: Int, b: Int, c: Int, equalOk: Boolean): Boolean = {
    /* BEGIN SOLUTION */
    (equalOk && ((a <= b) && (b <= c))) || (a < b && b < c)
    /* END SOLUTION */
  }

  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(inOrderEqual(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int],
      t.getParameter(2).asInstanceOf[Int], t.getParameter(3).asInstanceOf[Boolean]))
  }
}
