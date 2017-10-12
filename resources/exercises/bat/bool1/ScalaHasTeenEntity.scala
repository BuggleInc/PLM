package bat.bool1

import plm.universe.bat.{BatEntity, BatTest}

class ScalaHasTeenEntity extends BatEntity {

  /* BEGIN TEMPLATE */
  def hasTeen(a: Int, b: Int, c: Int): Boolean = {
    /* BEGIN SOLUTION */
    (a > 12 && a < 20) || (b > 12 && b < 20) || (c > 12 && c < 20)
    /* END SOLUTION */
  }
  /* END TEMPLATE */


  override def run(t: BatTest) {
    t.setResult(
      hasTeen(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int], t.getParameter(2).asInstanceOf[Int])
    )
  }
}
