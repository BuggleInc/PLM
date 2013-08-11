package lessons.welcome.array.averagevalue;

import java.util.Random;

import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class AverageValue extends BatExercise {

	public AverageValue(Lesson lesson) {
		super(lesson);
		
		int[] randomTab = new int[30];
		Random r = new Random();
		for (int i=0; i<randomTab.length; i++) 
			randomTab[i] = r.nextInt(35);
		
		BatWorld myWorld = new BatWorld("averageValue");
		myWorld.addTest(VISIBLE, new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 });
		myWorld.addTest(VISIBLE, randomTab);
		myWorld.addTest(VISIBLE, new int[] {1, 1, 2, 3, 1}) ;
		myWorld.addTest(VISIBLE, new int[] {1, 1, 2, 4, 1}) ;
		myWorld.addTest(VISIBLE, new int[] {1, 1, 2, 1, 2, 3}) ;
		myWorld.addTest(INVISIBLE, new int[] {1, 1, 2, 1, 2, 1}) ;
		myWorld.addTest(INVISIBLE, new int[] {1, 2, 3, 1, 2, 3}) ;
		myWorld.addTest(INVISIBLE, new int[] {1, 2, 3}) ;
		myWorld.addTest(INVISIBLE, new int[] {1, 1, 1}) ;
		myWorld.addTest(INVISIBLE, new int[] {1, 2}) ;
		myWorld.addTest(INVISIBLE, new int[] {42}) ;

		templatePython("averageValue", 
				"def averageValue(nums):\n",
				"  total = 0\n"+
				"  for i in range(len(nums)):\n" +
				"    total += nums[i]\n"+
				"  return total / len(nums)\n");
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
