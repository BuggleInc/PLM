/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaCigarPartyEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def cigarParty(cigars: Int, isWeekend: Boolean): Boolean = {
    /* BEGIN SOLUTION */
    (isWeekend && cigars >= 40) || (!isWeekend && (cigars >= 40) && (cigars <= 60))
    /* END SOLUTION */
  }

  /* END TEMPLATE */


  override def run(t: BatTest) {
    t.setResult(cigarParty(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Boolean]
    ))
  }
}
