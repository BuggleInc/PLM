package lessons.welcome.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class IcyHot extends BatExercise {
	
	public IcyHot(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new BatWorld("IcyHot");
		myWorld.addTest(VISIBLE, 120,-1);
		myWorld.addTest(VISIBLE, -1,120);
		myWorld.addTest(VISIBLE, 2,120);
		
		myWorld.addTest(INVISIBLE, -1,100);
		myWorld.addTest(INVISIBLE, -2,-2);
		myWorld.addTest(INVISIBLE, 120,120);

		setup(myWorld);
	}


	/* BEGIN SKEL */
	public void run(BatTest t) {
		t.setResult( icyHot((Integer)t.getParameter(0),(Integer)t.getParameter(1)) );		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean icyHot(int temp1, int temp2) {
	
		/* BEGIN SOLUTION */
	  return temp1<0&&temp2>100 || temp1>100&&temp2<0;
		/* END SOLUTION */
}
		/* END TEMPLATE */
}
