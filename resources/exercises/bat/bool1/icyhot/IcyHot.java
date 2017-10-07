package bat.bool1.icyhot;


import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class IcyHot extends ExerciseTemplated {

	public IcyHot() {
		super("IcyHot", "IcyHot");

		BatWorld myWorld = new BatWorld("icyHot");
		myWorld.addTest(BatTest.VISIBLE, 120,-1);
		myWorld.addTest(BatTest.VISIBLE, -1,120);
		myWorld.addTest(BatTest.VISIBLE, 2,120);

		myWorld.addTest(BatTest.INVISIBLE, -1,100);
		myWorld.addTest(BatTest.INVISIBLE, -2,-2);
		myWorld.addTest(BatTest.INVISIBLE, 120,120);

		/*templatePython("icyHot", new String[]{"Int","Int"},
				"def icyHot(temp1, temp2):\n",
				"   return temp1<0 and temp2>100 or temp1>100 and temp2<0\n");
		templateScala("icyHot",new String[]{"Int","Int"}, 
				"def icyHot(temp1:Int, temp2:Int):Boolean = {\n",
				"   return temp1<0 && temp2>100 || temp1>100 && temp2<0\n"+
				"}");
		*/
		setup(myWorld);
	}
}
