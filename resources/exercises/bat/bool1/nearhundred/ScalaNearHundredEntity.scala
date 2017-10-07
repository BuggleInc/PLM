package bat.bool1.nearhundred

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

/**
 * @author matthieu
 */
class ScalaNearHundredEntity extends BatEntity {
  override def run(t: BatTest) {
    t.setResult( nearHundred(t.getParameter(0).asInstanceOf[Int]))
  }
  
  /* BEGIN TEMPLATE */
  def nearHundred(n:Int): Boolean = {
    /* BEGIN SOLUTION */
    (90<=n && n<=110)||(190<=n&&n<=210)
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}