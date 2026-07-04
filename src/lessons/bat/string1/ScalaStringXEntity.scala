package lessons.bat.string1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaStringXEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( stringX(t.getParameter(0).asInstanceOf[String]) );
	}

	/* BEGIN TEMPLATE */
	def stringX(str:String):String = {
		/* BEGIN SOLUTION */
	  var res = ""
	  for (i <- 0 to str.length-1)
	    if (str(i) != 'x' || i == 0 || i == str.length-1)
	      res += str.substring(i,i+1)
	  return res
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
