package bat.bool1

import plm.universe.bat.{BatEntity, BatTest}

class ScalaClose10Entity extends BatEntity {
  /* BEGIN TEMPLATE */
  def close10(a: Int, b: Int): Int = {
    /* BEGIN SOLUTION */
    if (Math.abs(10 - a) == Math.abs(10 - b))
      0
    else if (Math.abs(10 - a) < Math.abs(10 - b))
      a
    else
      b
    /* END SOLUTION */
  }
  /* END TEMPLATE */


  override def run(t: BatTest) {
    t.setResult(close10(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]))
  }
}
