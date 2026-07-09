package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaPosNegEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(posNeg(param(0).asInstanceOf[Int],param(1).asInstanceOf[Int],param(2).asInstanceOf[Boolean]) ))
	  }
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
