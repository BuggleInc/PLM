package bat.bool1.makes10

import plm.universe.bat.BatTest
import plm.universe.bat.BatEntity

class ScalaMakes10Entity extends BatEntity {
  override def run(t: BatTest) {
    t.setResult( makes10(t.getParameter(0).asInstanceOf[Integer], t.getParameter(1).asInstanceOf[Integer]))
  }

  /* BEGIN TEMPLATE */
  def makes10(a:Int, b:Int): Boolean = {
    /* BEGIN SOLUTION */
    (a==10) || (b==10) || ((a+b)==10)
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}