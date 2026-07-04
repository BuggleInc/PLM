package lessons.bat.string1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaStringBitsEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( stringBits(t.getParameter(0).asInstanceOf[String]) );
	}

	/* BEGIN TEMPLATE */
	def stringBits(str:String):String = {
		/* BEGIN SOLUTION */
	  var res:String = ""
	  for (i <- 0 to str.length-1 by 2)
	    res += str.substring(i,i+1)
	  return res
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
