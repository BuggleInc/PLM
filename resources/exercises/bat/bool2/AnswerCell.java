/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class AnswerCell extends ExerciseTemplated {
	public AnswerCell(Lesson lesson) {
		super("AnswerCell");

		BatWorld myWorld = new BatWorld("answerCell");
		myWorld.addTest(BatTest.VISIBLE, false, false, false) ;
		myWorld.addTest(BatTest.VISIBLE, false, false, true) ;
		myWorld.addTest(BatTest.VISIBLE, true, false, false) ;
		myWorld.addTest(BatTest.INVISIBLE, true, true, false) ;
		myWorld.addTest(BatTest.INVISIBLE, false, true, false) ;
		myWorld.addTest(BatTest.INVISIBLE, true, true, true) ;

		templatePython("answerCell", new String[] {"Boolean","Boolean","Boolean"},
				"def answerCell(isMorning, isMom, isAsleep):\n",
				"   return (not isAsleep) and not (isMorning and not isMom)");
		templateScala("answerCell",new String[] {"Boolean","Boolean","Boolean"}, 
				"def answerCell(isMorning:Boolean, isMom:Boolean, isAsleep:Boolean):Boolean = {\n",
				"   return (! isAsleep) && !(isMorning && !isMom)\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( answerCell((Boolean)t.getParameter(0), (Boolean)t.getParameter(1), (Boolean)t.getParameter(2)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean answerCell(boolean isMorning, boolean isMom, boolean isAsleep) {
		/* BEGIN SOLUTION */
		return (! isAsleep) && ! (isMorning && ! isMom);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
