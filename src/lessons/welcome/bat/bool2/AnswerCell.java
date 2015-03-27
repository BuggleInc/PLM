/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class AnswerCell extends BatExercise {
	public AnswerCell(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "answerCell");
		myWorld.addTest(VISIBLE, false, false, false) ;
		myWorld.addTest(VISIBLE, false, false, true) ;
		myWorld.addTest(VISIBLE, true, false, false) ;
		myWorld.addTest(INVISIBLE, true, true, false) ;
		myWorld.addTest(INVISIBLE, false, true, false) ;
		myWorld.addTest(INVISIBLE, true, true, true) ;

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
