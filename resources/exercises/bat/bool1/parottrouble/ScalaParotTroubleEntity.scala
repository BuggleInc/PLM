package bat.bool1.parottrouble

import plm.universe.bat.BatTest
import plm.universe.bat.BatEntity

class ScalaParotTroubleEntity extends BatEntity {
  override def run(t: BatTest) {
    t.setResult( parotTrouble(t.getParameter(0).asInstanceOf[Boolean], t.getParameter(1).asInstanceOf[Int]) )
  }
  
  /* BEGIN TEMPLATE */
  def parotTrouble(talking:Boolean, hour:Int):Boolean = {
	  /* BEGIN SOLUTION */
    talking && (hour<7||hour>20)
    /* END SOLUTION */
	}
  /* END TEMPLATE */
}