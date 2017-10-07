package bat.bool1.sleepin

import plm.universe.bat.BatTest
import plm.universe.bat.BatEntity

/**
 * @author matthieu
 */
class ScalaSleepInEntity extends BatEntity {
  override def run(t: BatTest) {
    t.setResult( sleepIn(t.getParameter(0).asInstanceOf[Boolean],t.getParameter(1).asInstanceOf[Boolean]))
  }

  /* BEGIN TEMPLATE */
  def sleepIn(weekday: Boolean, vacation: Boolean): Boolean = {
    /* BEGIN SOLUTION */
    !weekday || vacation;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}