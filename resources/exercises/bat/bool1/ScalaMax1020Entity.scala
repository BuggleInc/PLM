package bat.bool1

import plm.universe.bat.{BatEntity, BatTest}

class ScalaMax1020Entity extends BatEntity {
  /* BEGIN TEMPLATE */
  def max1020(a: Int, b: Int): Int = {
    /* BEGIN SOLUTION */
    val A = Math.max(a, b)
    val B = Math.min(a, b)
    if (A < 21 && A > 9) {
      return A
    }
    if (B < 21 && B > 9) {
      return B
    }
    return 0
    /* END SOLUTION */
  }
  /* END TEMPLATE */


  override def run(t: BatTest) {
    t.setResult(max1020(t.getParameter(0).asInstanceOf[Int],
      t.getParameter(1).asInstanceOf[Int]))
  }
}
