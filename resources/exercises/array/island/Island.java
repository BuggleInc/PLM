package array.island;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Island extends ExerciseTemplated {
	
	public Island() {
		super("Island", "Island");

		BatWorld myWorld = new BatWorld("island");
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {0,1,2,2,1,0,1,2,2,1,0}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {0,1,2,3,4,3,2,1,2,3,4,3,2,1,0}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {0,0,0,0,0,0}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {0,0,2,4,0,0}) ;
		

		/*templatePython("island", new String[] {"Array[Int]"},
				"def island(num):\n",
				"  nbisland=0\n" +
				"  for i in range(len(num)-1):\n" +
				"    if num[i]<num[i+1]:\n" +
				"      nbisland=nbisland+1\n" +
				"  return nbisland\n"
				);
		templateScala("island", new String[] {"Array[Int]"}, 
				"def island(num:Array[Int]): Int = {\n",
				"  var nbIsland=0;\n" +
				"  for (i <- 0 to num.length-2){\n" +
				"    if (num(i)<num(i+1))\n"+
				"      nbIsland=nbIsland+1;\n" +
				"  }\n" +
				"  return nbIsland\n"+
				"}");
		*/
		setup(myWorld);
	}
}
