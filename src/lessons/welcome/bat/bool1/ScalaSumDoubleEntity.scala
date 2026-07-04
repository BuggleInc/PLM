package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaSumDoubleEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( sumDouble( t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def sumDouble(a: Integer, b: Integer): Integer = {
		/* BEGIN SOLUTION */
	  if (a==b) {
	    return (a+b)*2
	  }
	  return a+b
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
