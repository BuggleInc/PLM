package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaBlueTicketEntity extends BatEntity {

  /* BEGIN TEMPLATE */
  def blueTicket(a: Int, b: Int, c: Int): Int = {
    /* BEGIN SOLUTION */
    val ab = a + b
    val ac = a + c
    val bc = b + c
    if (ab == 10 || ac == 10 || bc == 10)
      10
    else if (ab == (bc + 10) || ab == (ac + 10))
      5
    else
      0
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(blueTicket(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int],
      t.getParameter(2).asInstanceOf[Int]
    ))
  }
}
