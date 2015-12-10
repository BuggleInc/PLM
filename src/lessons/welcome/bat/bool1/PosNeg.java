package lessons.welcome.bat.bool1;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class PosNeg extends BatExercise {

	public PosNeg(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "posNeg");
		myWorld.addTest(VISIBLE, -1,1,false);
		myWorld.addTest(VISIBLE, 1,-1,false);
		myWorld.addTest(VISIBLE, 1,1,false);

		myWorld.addTest(INVISIBLE, -1,-1,false);
		myWorld.addTest(INVISIBLE, 1,-1,true);
		myWorld.addTest(INVISIBLE, -1,1,true);
		myWorld.addTest(INVISIBLE, 1,1,true);
		myWorld.addTest(INVISIBLE, -1,-1,true);
		myWorld.addTest(INVISIBLE, 5,-5,true);
		myWorld.addTest(INVISIBLE, -6,6,false);
		myWorld.addTest(INVISIBLE, -5,-5,false);
		myWorld.addTest(INVISIBLE, -5,5,true);
		myWorld.addTest(INVISIBLE, -5,-5,true);

		templatePython("posNeg", new String[] { "Int","Int","Boolean"},
				"def posNeg(a, b, negative):\n",
				"		if (negative):\n"+
				"			return a<0 and b<0;\n"+
				"		return (a<0 and b>0) or (a>0 and b<0)");
		templateScala("posNeg", new String[] { "Int","Int","Boolean"},
				"def posNeg(a:Int, b:Int, negative:Boolean):Boolean = {\n",
				"	if (negative)\n"
			  + "      return a<0&&b<0;\n"
			  + "	return (a<0&&b>0) || (a>0&&b<0);\n"
			  + "}");
		setup(myWorld);
	}


	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( posNeg((Integer)t.getParameter(0),(Integer)t.getParameter(1),(Boolean)t.getParameter(2)) );		
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean posNeg(int a,int b,boolean negative) {

		/* BEGIN SOLUTION */
		if (negative)
			return a<0&&b<0;
		return (a<0&&b>0) || (a>0&&b<0);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
