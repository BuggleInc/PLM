package lessons.welcome.bat.bool1;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class SumDouble extends BatExercise {

	public SumDouble(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "sumDouble");
		myWorld.addTest(VISIBLE,  1,2);
		myWorld.addTest(VISIBLE,  3,2);
		myWorld.addTest(VISIBLE,  2,2);

		myWorld.addTest(INVISIBLE, -1,0);
		myWorld.addTest(INVISIBLE, 0,0);
		myWorld.addTest(INVISIBLE, 0,1);

		templatePython("sumDouble", new String[]{"Integer","Integer"},
				"def sumDouble(a, b):\n",
				"  if a==b:\n"+
				"    return (a+b)*2\n"+
				"  return a+b\n");
		
		templateScala("sumDouble", new String[]{"Integer","Integer"}, 
				"def sumDouble(a: Integer, b: Integer): Integer = {\n",
				"  if (a==b) {\n"+
				"    return (a+b)*2\n"+
				"  }\n"+
				"  return a+b\n"+
				"}");
			//"t.setResult( sumDouble( t.getParameter(0).asInstanceOf[Integer], t.getParameter(1).asInstanceOf[Integer] ) )");
		setup(myWorld);
	}


	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( sumDouble( ((Integer)(t.getParameter(0))),((Integer)(t.getParameter(1)))) );		
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int sumDouble(int a, int b) {
		/* BEGIN SOLUTION */
		if (a==b)
			return (a+b)*2;
		return a+b;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
