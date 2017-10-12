package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaRedTicketEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def redTicket(a: Int, b: Int, c: Int): Int = {
    /* BEGIN SOLUTION */
    if (a == b && b == c && c == 2)
      return 10
    else if (a == b && b == c)
      return 5
    else if (b != a && c != a)
      return 1
    else
      return 0
    /* END SOLUTION */
  }

  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(redTicket(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int],
      t.getParameter(2).asInstanceOf[Int]
    ) )
  }
}
