package lessons.welcome.bat.bool1;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class IcyHot extends BatExercise {

	public IcyHot(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "icyHot");
		myWorld.addTest(VISIBLE, 120,-1);
		myWorld.addTest(VISIBLE, -1,120);
		myWorld.addTest(VISIBLE, 2,120);

		myWorld.addTest(INVISIBLE, -1,100);
		myWorld.addTest(INVISIBLE, -2,-2);
		myWorld.addTest(INVISIBLE, 120,120);

		templatePython("icyHot", new String[]{"Int","Int"},
				"def icyHot(temp1, temp2):\n",
				"   return temp1<0 and temp2>100 or temp1>100 and temp2<0\n");
		templateScala("icyHot",new String[]{"Int","Int"}, 
				"def icyHot(temp1:Int, temp2:Int):Boolean = {\n",
				"   return temp1<0 && temp2>100 || temp1>100 && temp2<0\n"+
				"}");
		setup(myWorld);
	}


	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( icyHot((Integer)t.getParameter(0),(Integer)t.getParameter(1)) );		
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean icyHot(int temp1, int temp2) {

		/* BEGIN SOLUTION */
		return temp1<0&&temp2>100 || temp1>100&&temp2<0;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
