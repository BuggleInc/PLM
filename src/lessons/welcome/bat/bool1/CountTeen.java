package lessons.welcome.bat.bool1;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class CountTeen extends BatExercise {

	public CountTeen(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "countTeen");
		myWorld.addTest(VISIBLE,  13,20,10,54);
		myWorld.addTest(VISIBLE,  20,19,13,15);
		myWorld.addTest(VISIBLE,  20,10,13,42);

		myWorld.addTest(INVISIBLE, 1,20,12,54);
		myWorld.addTest(INVISIBLE, 19,20,42,12);
		myWorld.addTest(INVISIBLE, 12,16,20,19);
		myWorld.addTest(INVISIBLE, 42,12,9,20);
		myWorld.addTest(INVISIBLE, 12,18,19,14);
		myWorld.addTest(INVISIBLE, 14,2,20,99);
		myWorld.addTest(INVISIBLE, 4,11,2,20);
		myWorld.addTest(INVISIBLE, 11,11,11,11);
		myWorld.addTest(INVISIBLE, 15,15,15,15);

		templatePython("countTeen", new String[] {"Integer","Integer","Integer","Integer"},
				"def countTeen(a, b, c, d):\n",
				"		ret=0\n"+
				"		if (a>12 and a<20):\n"+
				"			ret+=1\n"+
				"		if (b>12 and b<20):\n"+
				"			ret+=1\n"+
				"		if (c>12 and c<20):\n"+
				"			ret+=1\n"+
				"		if (d>12 and d<20):\n"+
				"			ret+=1\n"+
				"		return ret\n");
		templateScala("countTeen", new String[] {"Integer","Integer","Integer","Integer"}, 
			"def countTeen(a:Int, b:Int,c:Int,d:Int): Int = {\n",
			  "  var ret=0;\n"
			+ "  if (a>12&&a<20)\n"
			+ "	   ret+=1;\n"
			+ "  if (b>12&&b<20)\n"
			+ "	   ret+=1;\n"
			+ "  if (c>12&&c<20)\n"
			+ "	   ret+=1;\n"
			+ "  if (d>12&&d<20)\n"
			+ "	   ret+=1;\n"
			+ "  return ret\n"
			+ "}");
		setup(myWorld);
	}


	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( countTeen((Integer)t.getParameter(0),(Integer)t.getParameter(1),(Integer)t.getParameter(2),(Integer)t.getParameter(3)) );		
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int countTeen(int a, int b, int c,int d) {
		/* BEGIN SOLUTION */
		int ret=0;
		if (a>12&&a<20)
			ret+=1;
		if (b>12&&b<20)
			ret+=1;
		if (c>12&&c<20)
			ret+=1;
		if (d>12&&d<20)
			ret+=1;
		return ret; 
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
