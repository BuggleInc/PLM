package lessons.welcome.array.golomb;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Golomb extends BatExercise {
	public Golomb(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "golomb");
		myWorld.addTest(VISIBLE, (Object)new Integer(1)) ;
		myWorld.addTest(VISIBLE, (Object)new Integer(2)) ;
		myWorld.addTest(VISIBLE, (Object)new Integer(3)) ;
		myWorld.addTest(VISIBLE, (Object)new Integer(4)) ;
		myWorld.addTest(VISIBLE, (Object)new Integer(5)) ;
		myWorld.addTest(VISIBLE, (Object)new Integer(6)) ;
		myWorld.addTest(VISIBLE, (Object)new Integer(7)) ;
		myWorld.addTest(VISIBLE, (Object)new Integer(8)) ;
		myWorld.addTest(VISIBLE, (Object)new Integer(9)) ;
		myWorld.addTest(VISIBLE, (Object)new Integer(10)) ;
		myWorld.addTest(VISIBLE, (Object)new Integer(11)) ;
		myWorld.addTest(VISIBLE, (Object)new Integer(12)) ;
		myWorld.addTest(VISIBLE, (Object)new Integer(13)) ;
		myWorld.addTest(INVISIBLE, (Object)new Integer(14)) ;
		myWorld.addTest(INVISIBLE, (Object)new Integer(15)) ;
		myWorld.addTest(INVISIBLE, (Object)new Integer(16)) ;
		myWorld.addTest(INVISIBLE, (Object)new Integer(17)) ;
		myWorld.addTest(INVISIBLE, (Object)new Integer(18)) ;
		myWorld.addTest(INVISIBLE, (Object)new Integer(19)) ;
		myWorld.addTest(INVISIBLE, (Object)new Integer(20)) ;
		

		templatePython("golomb", new String[] {"Int"},
				"def golomb(num):\n",
				"  if num==1:\n" +
				"    return 1\n"+
				"  else:\n"+
				"    return 1+golomb(num-golomb(golomb(num-1)))\n");
		templateScala("golomb", new String[] {"Int"}, 
				"def golomb(num:Int): Int = {\n",
				"  if(num==1)\n" +
				"  		return 1;\n"+
				"  else\n"+
				"  		return 1+golomb(num-golomb(golomb(num-1)));\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( golomb((int)t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int golomb(int num) {
		/* BEGIN SOLUTION */
		if(num==1){
			return 1;
		}else{
			return 1 + golomb( num - golomb( golomb( num-1 ) ) );
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}

