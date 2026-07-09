package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaWithoutDoublesEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(withoutDoubles(param(0).asInstanceOf[Int], param(1).asInstanceOf[Int], param(2).asInstanceOf[Boolean]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def withoutDoubles(die1:Int, die2:Int, noDoubles:Boolean):Int = {
		/* BEGIN SOLUTION */
		if (noDoubles && (die1 == die2)) {
			if (die1 == 6)
				return 1 + die2
			else
				return die1 + 1 + die2
		} else
			return die1 + die2
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
