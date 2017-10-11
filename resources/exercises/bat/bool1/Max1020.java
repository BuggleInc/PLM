package bat.bool1;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Max1020 extends ExerciseTemplated {
	public Max1020(Lesson lesson) {
		super("Max1020");

		BatWorld myWorld = new BatWorld("max1020");
		myWorld.addTest(BatTest.VISIBLE, 11, 19) ;
		myWorld.addTest(BatTest.VISIBLE, 19, 11) ;
		myWorld.addTest(BatTest.VISIBLE, 11, 9) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, 21) ;
		myWorld.addTest(BatTest.INVISIBLE, 10, 21) ;
		myWorld.addTest(BatTest.INVISIBLE, 21, 10) ;
		myWorld.addTest(BatTest.INVISIBLE, 9, 11) ;
		myWorld.addTest(BatTest.INVISIBLE, 23, 10) ;
		myWorld.addTest(BatTest.INVISIBLE, 20, 10) ;
		myWorld.addTest(BatTest.INVISIBLE, 7, 20) ;
		myWorld.addTest(BatTest.INVISIBLE, 17, 16) ;

		templatePython("max1020", new String[] {"Int","Int"},
				"def max1020(a, b):\n",
				"	A = max(a,b)\n"+
				"	B = min(a,b)\n"+
				"	if (A<21 and A>9):\n"+
				"		return A\n"+
				"	if (B<21 and B>9):\n"+
				"		return B\n"+
				"	return 0\n");
		templateScala("max1020", new String[] {"Int","Int"}, 
				"def max1020(a:Int, b:Int):Int = {\n",
				"	val A = Math.max(a,b)\n"+
				"	val B = Math.min(a,b)\n"+
				"	if (A<21 && A>9)\n"+
				"		return A\n"+
				"	if (B<21 && B>9)\n"+
				"		return B\n"+
				"	return 0\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( max1020((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int max1020(int a, int b) {
		/* BEGIN SOLUTION */
		int A = a>b?a:b;
		int B = a>b?b:a;
		if (A<21 && A>9)
			return A;
		if (B<21 && B>9)
			return B;
		return 0;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
