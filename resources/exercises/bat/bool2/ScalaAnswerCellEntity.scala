/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaAnswerCellEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def answerCell(isMorning: Boolean, isMom: Boolean, isAsleep: Boolean): Boolean = {
    /* BEGIN SOLUTION */
    (!isAsleep) && !(isMorning && !isMom)
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(answerCell(t.getParameter(0).asInstanceOf[Boolean], t.getParameter(1).asInstanceOf[Boolean], t.getParameter(2).asInstanceOf[Boolean]
    ))
  }
}
