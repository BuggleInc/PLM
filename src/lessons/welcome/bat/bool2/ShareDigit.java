/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ShareDigit extends BatExercise {
	public ShareDigit(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "shareDigit");
		myWorld.addTest(VISIBLE, 12, 23) ;
		myWorld.addTest(VISIBLE, 12, 43) ;
		myWorld.addTest(VISIBLE, 12, 44) ;
		myWorld.addTest(INVISIBLE, 23, 12) ;
		myWorld.addTest(INVISIBLE, 23, 39) ;
		myWorld.addTest(INVISIBLE, 23, 19) ;
		myWorld.addTest(INVISIBLE, 30, 90) ;
		myWorld.addTest(INVISIBLE, 30, 91) ;
		myWorld.addTest(INVISIBLE, 55, 55) ;
		myWorld.addTest(INVISIBLE, 55, 44) ;

		templatePython("shareDigit", new String[]{"Int","Int"},
				"def shareDigit(a, b):\n",
				"   return (a/10 == b/10 or a/10 == b%10 or a%10 == b/10 or a%10 == b%10)");
		templateScala("shareDigit", new String[]{"Int","Int"}, 
				"def shareDigit(a:Int, b:Int):Boolean = {\n",
				"   return (a/10 == b/10 || a/10 == b%10 || a%10 == b/10 || a%10 == b%10)\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( shareDigit((Integer)t.getParameter(0), (Integer)t.getParameter(1)) ); 
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean shareDigit(int a, int b) {
		/* BEGIN SOLUTION */
		return (a/10 == b/10 || a/10 == b%10 || a%10 == b/10 || a%10 == b%10);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
