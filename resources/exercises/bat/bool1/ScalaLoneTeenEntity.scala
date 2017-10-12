package bat.bool1

import plm.universe.bat.{BatEntity, BatTest}

class ScalaLoneTeenEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def loneTeen(a: Int, b: Int): Boolean = {
    /* BEGIN SOLUTION */
    val teenA = a > 12 && a < 20
    val teenB = b > 12 && b < 20
    (teenA && !teenB) || (teenB && !teenA)
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(loneTeen(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]))
  }
}
