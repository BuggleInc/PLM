package lessons.welcome.conditions.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class PosNeg extends BatExercise {

	public PosNeg(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("posNeg");
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

		templatePython("posNeg", 
				"def posNeg(a, b, negative):\n",
				"		if (negative):\n"+
				"			return a<0 and b<0;\n"+
				"		return (a<0 and b>0) or (a>0 and b<0)");
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
