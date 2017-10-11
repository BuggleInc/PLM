package array.golomb;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Golomb extends ExerciseTemplated {
	public Golomb(Lesson lesson) {
		super("Golomb");

		BatWorld myWorld = new BatWorld("golomb");
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(1)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(2)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(3)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(4)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(5)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(6)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(7)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(8)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(9)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(10)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(11)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(12)) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new Integer(13)) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new Integer(14)) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new Integer(15)) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new Integer(16)) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new Integer(17)) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new Integer(18)) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new Integer(19)) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new Integer(20)) ;
		

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

