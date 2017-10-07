package bat.bool1.diff21

import plm.universe.bat.BatTest
import plm.universe.bat.BatEntity

/**
 * @author matthieu
 */
class ScalaDiff21Entity extends BatEntity {
  override def run(t: BatTest) {
    t.setResult( diff21(t.getParameter(0).asInstanceOf[Integer]))
  }

  /* BEGIN TEMPLATE */
  def diff21(n:Int): Int = {
    /* BEGIN SOLUTION */
    if (n>21) {
      2*(n-21)
    }
    else {
      21 - n
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
