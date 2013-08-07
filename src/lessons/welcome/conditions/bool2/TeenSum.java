/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.conditions.bool2;
import jlm.core.model.Game;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class TeenSum extends BatExercise {
	public TeenSum(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("teenSum");
		myWorld.addTest(VISIBLE, 3, 4) ;
		myWorld.addTest(VISIBLE, 10, 13) ;
		myWorld.addTest(VISIBLE, 13, 2) ;
		myWorld.addTest(INVISIBLE, 3, 19) ;
		myWorld.addTest(INVISIBLE, 13, 13) ;
		myWorld.addTest(INVISIBLE, 10, 10) ;
		myWorld.addTest(INVISIBLE, 6, 14) ;
		myWorld.addTest(INVISIBLE, 15, 2) ;
		myWorld.addTest(INVISIBLE, 19, 19) ;
		myWorld.addTest(INVISIBLE, 19, 20) ;
		myWorld.addTest(INVISIBLE, 2, 18) ;
		myWorld.addTest(INVISIBLE, 12, 4) ;
		myWorld.addTest(INVISIBLE, 2, 20) ;
		myWorld.addTest(INVISIBLE, 2, 17) ;
		myWorld.addTest(INVISIBLE, 2, 16) ;
		myWorld.addTest(INVISIBLE, 6, 7) ;

		langTemplate(Game.PYTHON, "teenSum", 
				"def teenSum(a, b):\n",
				"	if ((a >= 13 and a <= 19) or (b >= 13 and b <= 19)):\n"+
				"		return 19\n"+
				"	else:\n"+
				"		return a+b\n");
		setup(myWorld);
	}

	/* BEGIN SKEL */
	public void run(BatTest t) {
		t.setResult( teenSum((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
	}
	/* END SKEL */

	/* BEGIN TEMPLATE */
	int teenSum(int a, int b) {
		/* BEGIN SOLUTION */
		if ((a >= 13 && a <= 19) || (b >= 13 && b <= 19))
			return 19;
		else
			return a+b;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
