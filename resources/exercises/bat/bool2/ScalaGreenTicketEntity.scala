/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaGreenTicketEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def greenTicket(a: Int, b: Int, c: Int): Int = {
    /* BEGIN SOLUTION */
    if (a == b && b == c)
      20
    else if (a == b || b == c || a == c)
      10
    else
      0
    /* END SOLUTION */
  }

  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(greenTicket(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int], t.getParameter(2).asInstanceOf[Int]));
  }
}
