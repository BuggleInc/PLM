/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaTeaPartyEntity extends BatEntity {
  override def run(t: BatTest) {
    t.setResult(teaParty(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]))
  }

  /* BEGIN TEMPLATE */
  def teaParty(tea: Int, candy: Int): Int = {
    /* BEGIN SOLUTION */
    if (tea < 5 || candy < 5)
      0
    else if (tea >= 2 * candy || candy >= 2 * tea)
      2
    else
      1
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
