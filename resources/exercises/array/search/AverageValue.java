package array.search;

import java.util.Random;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class AverageValue extends ExerciseTemplated {

	public AverageValue(Lesson lesson) {
		super("AverageValue");
		
		int[] randomTab = new int[30];
		Random r = new Random();
		for (int i=0; i<randomTab.length; i++) 
			randomTab[i] = r.nextInt(35);
		
		BatWorld myWorld = new BatWorld("averageValue");
		myWorld.addTest(BatTest.VISIBLE, new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 });
		myWorld.addTest(BatTest.VISIBLE, randomTab);
		myWorld.addTest(BatTest.VISIBLE, new int[] {1, 1, 2, 3, 1}) ;
		myWorld.addTest(BatTest.VISIBLE, new int[] {1, 1, 2, 4, 1}) ;
		myWorld.addTest(BatTest.VISIBLE, new int[] {1, 1, 2, 1, 2, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, new int[] {1, 1, 2, 1, 2, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, new int[] {1, 2, 3, 1, 2, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, new int[] {1, 2, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, new int[] {1, 1, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, new int[] {1, 2}) ;
		myWorld.addTest(BatTest.INVISIBLE, new int[] {42}) ;

		templatePython("averageValue", new String[] {"Array[Int]"},
				"def averageValue(nums):\n",
				"  total = 0\n"+
				"  for i in range(len(nums)):\n" +
				"    total += nums[i]\n"+
				"  return total / len(nums)\n");
		templateScala("averageValue",new String[] {"Array[Int]"}, 
				"def averageValue(nums:Array[Int]): Int = {\n",
				"  var total = 0\n"+
				"  for (i <- 0 to nums.length -1) \n" +
				"    total += nums(i)\n"+
				"  return total / nums.length\n"+
				"}");
		setup(myWorld);		
	}
	

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( averageValue((int[])t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int averageValue(int[] nums) {
		/* BEGIN SOLUTION */
		int total = 0;
		for (int i=0; i < nums.length; i++) 
			total += nums[i];
		return total / nums.length;
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
