package lessons.bat.string1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaStringMatchEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( stringMatch(t.getParameter(0).asInstanceOf[String], t.getParameter(1).asInstanceOf[String]) );
	}

	/* BEGIN TEMPLATE */
	def stringMatch(a:String, b:String):Int = {
	  /* BEGIN SOLUTION */
	  val l = Math.min( a.length, b.length )
	  var count = 0
	  for (i <- 0 to l-2)
	    if (a.substring(i,i+2) == b.substring(i,i+2))
	      count += 1
	  return count
	  /* END SOLUTION */
	}
	/* END TEMPLATE */
}
