package bat.bool1

import plm.universe.bat.{BatEntity, BatTest}

class ScalaIn3050Entity extends BatEntity {
  /* BEGIN TEMPLATE */
  def in3050(a: Int, b: Int): Boolean = {
    /* BEGIN SOLUTION */
    (a > 29 && a < 41 && b > 29 && b < 41) || (a > 39 && a < 51 && b > 39 && b < 51)
    /* END SOLUTION */
  }

  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(in3050(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]))
  }
}
