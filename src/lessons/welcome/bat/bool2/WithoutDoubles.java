/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class WithoutDoubles extends BatExercise {
	public WithoutDoubles(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "withoutDoubles");
		myWorld.addTest(VISIBLE, 2, 3, true) ;
		myWorld.addTest(VISIBLE, 3, 3, true) ;
		myWorld.addTest(VISIBLE, 3, 3, false) ;
		myWorld.addTest(INVISIBLE, 2, 3, false) ;
		myWorld.addTest(INVISIBLE, 5, 4, true) ;
		myWorld.addTest(INVISIBLE, 5, 4, false) ;
		myWorld.addTest(INVISIBLE, 5, 5, true) ;
		myWorld.addTest(INVISIBLE, 5, 5, false) ;
		myWorld.addTest(INVISIBLE, 6, 6, true) ;
		myWorld.addTest(INVISIBLE, 6, 6, false) ;
		myWorld.addTest(INVISIBLE, 1, 6, true) ;
		myWorld.addTest(INVISIBLE, 6, 1, false) ;

		templatePython("withoutDoubles", new String[]{"Int","Int","Boolean"},
				"def withoutDoubles(die1, die2, noDoubles):\n",
				"	if (noDoubles and (die1 == die2)):\n"+
				"		if (die1 == 6):\n"+
				"			return 1 + die2\n"+
				"		else:\n"+
				"			return die1 + 1 + die2\n"+
				"	else:\n"+
				"		return die1 + die2\n");
		templateScala("withoutDoubles", new String[]{"Int","Int","Boolean"},
				"def withoutDoubles(die1:Int, die2:Int, noDoubles:Boolean):Int = {\n",
				"	if (noDoubles && (die1 == die2)) {\n"+
				"		if (die1 == 6)\n"+
				"			return 1 + die2\n"+
				"		else\n"+
				"			return die1 + 1 + die2\n"+
				"	} else\n"+
				"		return die1 + die2\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( withoutDoubles((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Boolean)t.getParameter(2)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int withoutDoubles(int die1, int die2, boolean noDoubles) {
		/* BEGIN SOLUTION */
		if (noDoubles && (die1 == die2)) {
			if (die1 == 6)
				return 1 + die2;
			else 
				return die1 + 1 + die2;
		} else 
			return die1 + die2;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
