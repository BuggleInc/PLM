package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaIn1To10Entity extends BatEntity {

  /* BEGIN TEMPLATE */
  def in1To10(n: Int, outsideMode: Boolean): Boolean = {
    /* BEGIN SOLUTION */
    (outsideMode && (n <= 1 || n >= 10)) || ((!outsideMode) && (n >= 1 && n <= 10))
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(in1To10(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Boolean]
    ) )
  }
}
