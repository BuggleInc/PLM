package lessons.welcome.array.search;

import java.util.Random;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class SecondMaxValue extends BatExercise {

	public SecondMaxValue(Lesson lesson) {
		super(lesson);
		Random r = new Random();
		
		int[] tab = new int[15];
		for (int i=0; i<tab.length; i++) 
			tab[i] = r.nextInt(35);

		int[] tab2 = new int[25];
		for (int i=0; i<tab2.length; i++) 
			tab2[i] = r.nextInt(35);

		int[] tab3 = new int[25];
		for (int i=0; i<tab3.length; i++) 
			tab3[i] = r.nextInt(35)-15;
		
		int[] tab4 = new int[25];
		for (int i=0; i<tab4.length; i++) 
			tab4[i] = r.nextInt(35)-15;

		BatWorld myWorld = new BatWorld("max2Value");
		myWorld.addTest(VISIBLE, new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 }) ;
		myWorld.addTest(VISIBLE, tab) ;
		myWorld.addTest(VISIBLE, tab2) ;
		myWorld.addTest(INVISIBLE, tab3) ;
		myWorld.addTest(INVISIBLE, tab4) ;

		templatePython("max2Value", new String[] {"Array[Int]"},
				"def max2Value(nums):\n",
				"  max=nums[0]\n"+
				"  sec=nums[1]\n"+
				"  if (sec > max):\n"+
				"    max=nums[1]\n"+
				"    sec=nums[0]\n"+
				"  for i in range(len(nums)):\n"+
				"    if nums[i] > max:\n"+
				"      sec = max\n"+
				"      max = nums[i]\n"+
				"  return sec\n");
		templateScala("max2Value", new String[] {"Array[Int]"}, 
				"def max2Value(nums:Array[Int]): Int = {\n",
				"  var max=if (nums(0)>nums(1)) { nums(0) } else { nums(1) } \n"+
				"  var sec=if (nums(0)>nums(1)) { nums(1) } else { nums(2) } \n"+
				"  for (i <- 0 to nums.length-1)\n"+
				"    if (nums(i) > max) {\n"+
				"      sec = max\n"+
				"      max = nums(i)\n"+
				"    }\n"+
				"  return sec\n"+
				"}");

		setup(myWorld);
	}
	
	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( max2Value( (int[])t.getParameter(0) ));
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	// computes the index of the second maximum of the values contained in tab variable
	public int max2Value(int[] tab) {
		/* BEGIN SOLUTION */
		int max = tab[0];
		int sec = tab[1];
		if (sec > max) {
			max = tab[1];
			sec = tab[0];
		}
		for (int i=1; i<tab.length; i++) 
			if (tab[i] > max) {
				sec = max;
				max = tab[i];
			}
				
		return sec;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}




