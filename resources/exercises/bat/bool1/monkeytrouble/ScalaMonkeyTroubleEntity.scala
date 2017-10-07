package bat.bool1.monkeytrouble

import plm.universe.bat.BatTest
import plm.universe.bat.BatEntity

/**
 * @author matthieu
 */
class ScalaSleepInEntity extends BatEntity {
  override def run(t: BatTest) {
    t.setResult( monkeyTrouble(t.getParameter(0).asInstanceOf[Boolean],t.getParameter(1).asInstanceOf[Boolean]))
  }

  /* BEGIN TEMPLATE */
  def monkeyTrouble(aSmile:Boolean, bSmile:Boolean): Boolean = {
    /* BEGIN SOLUTION */
    ((aSmile && bSmile) || (!aSmile && !bSmile))
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}