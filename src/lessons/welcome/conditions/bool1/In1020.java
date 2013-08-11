package lessons.welcome.conditions.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class In1020 extends BatExercise {

	public In1020(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("in1020");
		myWorld.addTest(VISIBLE,  12,99);
		myWorld.addTest(VISIBLE,  21,12);
		myWorld.addTest(VISIBLE,  8,99);

		myWorld.addTest(INVISIBLE, 99,10);
		myWorld.addTest(INVISIBLE, 20,20);
		myWorld.addTest(INVISIBLE, 21,21);
		myWorld.addTest(INVISIBLE, 9,9);
		myWorld.addTest(INVISIBLE, 10,42);
		myWorld.addTest(INVISIBLE, 12,-2);

		templatePython("in1020", 
				"def in1020(a, b):\n",
				"   return (a>9 and a<21) or (b>9 and b<21)");
		setup(myWorld);
	}


	@Override
	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( in1020((Integer)t.getParameter(0),(Integer)t.getParameter(1)) );		
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean in1020(int a, int b) {
		/* BEGIN SOLUTION */
		return a>9&&a<21 || b>9&&b<21;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
