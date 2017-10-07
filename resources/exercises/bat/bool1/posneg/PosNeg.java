package bat.bool1.posneg;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class PosNeg extends ExerciseTemplated {

	public PosNeg() {
		super("PosNeg", "PosNeg");

		BatWorld myWorld = new BatWorld("posNeg");
		myWorld.addTest(BatTest.VISIBLE, -1,1,false);
		myWorld.addTest(BatTest.VISIBLE, 1,-1,false);
		myWorld.addTest(BatTest.VISIBLE, 1,1,false);

		myWorld.addTest(BatTest.INVISIBLE, -1,-1,false);
		myWorld.addTest(BatTest.INVISIBLE, 1,-1,true);
		myWorld.addTest(BatTest.INVISIBLE, -1,1,true);
		myWorld.addTest(BatTest.INVISIBLE, 1,1,true);
		myWorld.addTest(BatTest.INVISIBLE, -1,-1,true);
		myWorld.addTest(BatTest.INVISIBLE, 5,-5,true);
		myWorld.addTest(BatTest.INVISIBLE, -6,6,false);
		myWorld.addTest(BatTest.INVISIBLE, -5,-5,false);
		myWorld.addTest(BatTest.INVISIBLE, -5,5,true);
		myWorld.addTest(BatTest.INVISIBLE, -5,-5,true);

		/*
		templatePython("posNeg", new String[] { "Int","Int","Boolean"},
				"def posNeg(a, b, negative):\n",
				"		if (negative):\n"+
				"			return a<0 and b<0;\n"+
				"		return (a<0 and b>0) or (a>0 and b<0)");
		templateScala("posNeg", new String[] { "Int","Int","Boolean"},
				"def posNeg(a:Int, b:Int, negative:Boolean):Boolean = {\n",
				"	if (negative)\n"
			  + "      return a<0&&b<0;\n"
			  + "	return (a<0&&b>0) || (a>0&&b<0);\n"
			  + "}");
		*/
		setup(myWorld);
	}
}
