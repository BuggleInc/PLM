package bat.bool1

import plm.universe.bat.{BatEntity, BatTest}

class ScalaLastDigitEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def lastDigit(a: Int, b: Int): Boolean = {
    /* BEGIN SOLUTION */
    a % 10 == b % 10
    /* END SOLUTION */
  }

  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(lastDigit(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]))
  }
}
