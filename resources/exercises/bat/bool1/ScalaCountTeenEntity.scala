package bat.bool1

import plm.universe.bat.{BatEntity, BatTest}

class ScalaCountTeenEntity extends BatEntity {

  /* BEGIN TEMPLATE */
  def countTeen(a: Int, b: Int, c: Int, d: Int): Int = {
    /* BEGIN SOLUTION */
    var ret = 0
    if (a > 12 && a < 20)
      ret += 1
    if (b > 12 && b < 20)
      ret += 1
    if (c > 12 && c < 20)
      ret += 1
    if (d > 12 && d < 20)
      ret += 1
    ret
    /* END SOLUTION */
  }

  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(countTeen(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int], t.getParameter(2).asInstanceOf[Int], t.getParameter(3).asInstanceOf[Int]))
  }
}
