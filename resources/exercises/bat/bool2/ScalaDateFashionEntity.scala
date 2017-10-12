/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaDateFashionEntity extends BatEntity {

  /* BEGIN TEMPLATE */
  def dateFashion(you: Int, date: Int): Int = {
    /* BEGIN SOLUTION */
    if (you <= 2 || date <= 2)
      0
    else if (you >= 8 || date >= 8)
      2
    else
      1
    /* END SOLUTION */
  }

  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(dateFashion(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]
    ) )
  }
}
