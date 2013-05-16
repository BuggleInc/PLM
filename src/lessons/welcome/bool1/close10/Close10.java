package lessons.welcome.bool1.close10;

import jlm.core.model.Game;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class Close10 extends BatExercise {
	
	public Close10(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new BatWorld("close10");
		
		myWorld.addTest(VISIBLE,  8,13);
		myWorld.addTest(VISIBLE,  13,8);
		myWorld.addTest(VISIBLE,  13,7);
		
		myWorld.addTest(INVISIBLE, 7,13);
		myWorld.addTest(INVISIBLE, 5,21);
		myWorld.addTest(INVISIBLE, 0,20);
		myWorld.addTest(INVISIBLE, 10,10);

		setup(myWorld);
		langTemplate(Game.PYTHON, "close10", "import math\ndef close10(a,b):\n");
	}


	/* BEGIN SKEL */
	public void run(BatTest t) {
		t.setResult(close10((Integer)t.getParameter(0),(Integer)t.getParameter(1)));
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
int close10(int a, int b) {
	/* BEGIN SOLUTION */
	if (Math.abs(a-10)==Math.abs(b-10))
		return 0;
	if (Math.abs(a-10)<Math.abs(b-10))
		return a;
	return b;
	/* END SOLUTION */
}
		/* END TEMPLATE */
}
