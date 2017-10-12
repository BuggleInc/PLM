package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaSquirrelPlayEntity extends BatEntity {

  /* BEGIN TEMPLATE */
  def squirrelPlay(temp: Int, isSummer: Boolean): Boolean = {
    /* BEGIN SOLUTION */
    (temp >= 60 && ((isSummer && temp <= 100) || temp <= 90))
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(squirrelPlay(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Boolean]) )
  }
}
