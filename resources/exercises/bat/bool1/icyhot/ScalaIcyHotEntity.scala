package bat.bool1.icyhot

import plm.universe.bat.BatTest
import plm.universe.bat.BatEntity

class ScalaIcyHotEntity extends BatEntity {
  override def run(t: BatTest) {
    t.setResult( icyHot(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]) )
  }
  
  /* BEGIN TEMPLATE */
  def icyHot(temp1:Int, temp2: Int): Boolean = {
    /* BEGIN SOLUTION */
    temp1<0 && temp2>100 || temp1>100 && temp2<0
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}