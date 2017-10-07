package bat.bool1.posneg

import plm.universe.bat.BatTest
import plm.universe.bat.BatEntity

class ScalaPosNegEntity extends BatEntity {
  override def run(t: BatTest) {
    t.setResult( posNeg(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int], t.getParameter(2).asInstanceOf[Boolean]) )
  }
  
  /* BEGIN TEMPLATE */
  def posNeg(a:Int, b:Int, negative:Boolean):Boolean = {
    /* BEGIN SOLUTION */
  	if (negative) {
  	  a<0 && b<0;
  	} else {
  	  (a<0 && b>0) || (a>0 && b<0)
  	}
  	/* END SOLUTION */
  }
  /* END TEMPLATE */
}