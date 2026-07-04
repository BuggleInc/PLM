package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaAnswerCellEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( answerCell(t.getParameter(0).asInstanceOf[Boolean], t.getParameter(1).asInstanceOf[Boolean], t.getParameter(2).asInstanceOf[Boolean]) );
	}

	/* BEGIN TEMPLATE */
	def answerCell(isMorning:Boolean, isMom:Boolean, isAsleep:Boolean):Boolean = {
	/* BEGIN SOLUTION */
   return (! isAsleep) && !(isMorning && !isMom)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
