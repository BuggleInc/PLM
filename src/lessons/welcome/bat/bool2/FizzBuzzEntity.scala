package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class FizzBuzzEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(fizzBuzz(param(0).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def fizzBuzz(a:Int):String = {
		/* BEGIN SOLUTION */
		(a%5, a%3) match {	    case (0,0) => return "Fizz Buzz"
		    case (_,0) => return "Fizz"
	 	    case (0,_) => return "Buzz"
		    case _     => return ""+a
	    }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
