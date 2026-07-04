package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaPosNegEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( posNeg(t.getParameter(0).asInstanceOf[Int],t.getParameter(1).asInstanceOf[Int],t.getParameter(2).asInstanceOf[Boolean]) );
	}

	/* BEGIN TEMPLATE */
	def posNeg(a:Int, b:Int, negative:Boolean):Boolean = {
	/* BEGIN SOLUTION */
	if (negative)
      return a<0&&b<0;
	return (a<0&&b>0) || (a>0&&b<0);
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
