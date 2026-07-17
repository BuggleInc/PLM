package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class AnswerCellEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(answerCell(param(0).asInstanceOf[Boolean], param(1).asInstanceOf[Boolean], param(2).asInstanceOf[Boolean]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def answerCell(isMorning:Boolean, isMom:Boolean, isAsleep:Boolean):Boolean = {
	/* BEGIN SOLUTION */
   return (! isAsleep) && !(isMorning && !isMom)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
