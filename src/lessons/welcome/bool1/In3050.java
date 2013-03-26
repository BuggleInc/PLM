package lessons.welcome.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class In3050 extends BatExercise {
	
	public In3050(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new BatWorld("In3050");
		myWorld.addTest(VISIBLE,  30,31);
		myWorld.addTest(VISIBLE,  30,41);
		myWorld.addTest(VISIBLE,  40,50);
		
		myWorld.addTest(INVISIBLE, 40,51);
		myWorld.addTest(INVISIBLE, 39,50);
		myWorld.addTest(INVISIBLE, 50,39);
		myWorld.addTest(INVISIBLE, 40,39);
		myWorld.addTest(INVISIBLE, 49,48);
		myWorld.addTest(INVISIBLE, 50,40);
		myWorld.addTest(INVISIBLE, 50,51);
		myWorld.addTest(INVISIBLE, 35,36);
		myWorld.addTest(INVISIBLE, 35,45);


		setup(myWorld);
	}


	/* BEGIN SKEL */
	public void run(BatTest t) {
		t.setResult( in3050((Integer)t.getParameter(0),(Integer)t.getParameter(1)) );		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean in3050(int a, int b) {
	/* BEGIN SOLUTION */
		  return (a>29&&a<41 && b>29&&b<41) || (a>39&&a<51 && b>39&&b<51);
	/* END SOLUTION */
}
		/* END TEMPLATE */
}
