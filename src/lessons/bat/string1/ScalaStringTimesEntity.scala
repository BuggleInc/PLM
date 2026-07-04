package lessons.bat.string1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaStringTimesEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( stringTimes(t.getParameter(0).asInstanceOf[String], t.getParameter(1).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def stringTimes(str:String, n:Int):String = {
		/* BEGIN SOLUTION */
	  var res = ""
	  for (i <- 1 to n)
	    res ++= str
	  return res
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
