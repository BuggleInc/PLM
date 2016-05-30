package lessons.welcome.array.search;

import java.util.Random;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class MaxValue extends BatExercise {

	public MaxValue(Game game, Lesson lesson) {
		super(game, lesson);
		Random r = new Random(0);
		
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

		BatWorld myWorld = new BatWorld(game, "maxValue");
		myWorld.addTest(VISIBLE, new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 }) ;
		myWorld.addTest(VISIBLE, tab) ;
		myWorld.addTest(VISIBLE, tab2) ;
		myWorld.addTest(INVISIBLE, tab3) ;
		myWorld.addTest(INVISIBLE, tab4) ;

		templatePython("maxValue", new String[] {"Array[Int]"},
				"def maxValue(nums):\n",
				"  max=nums[0]\n"+
				"  for i in range(len(nums)):\n"+
				"    if nums[i] > max:\n"+
				"      max = nums[i]\n"+
				"  return max\n");
		templateScala("maxValue", new String[] {"Array[Int]"}, 
				"def maxValue(nums:Array[Int]): Int = {\n",
				"  var max=nums(0)\n"+
				"  for (i <- 0 to nums.length-1)\n"+
				"    if (nums(i) > max)\n"+
				"      max = nums(i)\n"+
				"  return max\n"+
				"}");

		setup(myWorld);
	}
	
	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( maxValue( (int[])t.getParameter(0) ));
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	// computes the index of the maximum of the values contained in tab variable
	public int maxValue(int[] tab) {
		/* BEGIN SOLUTION */
		int max = tab[0];
		for (int i=1; i<tab.length; i++) 
			if (tab[i] >= max)  
				max = tab[i];
				
		return max;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}




