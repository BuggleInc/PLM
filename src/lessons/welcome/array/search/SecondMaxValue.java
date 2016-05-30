package lessons.welcome.array.search;

import java.util.Random;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class SecondMaxValue extends BatExercise {

	public SecondMaxValue(Game game, Lesson lesson) {
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

		BatWorld myWorld = new BatWorld(game, "max2Value");
		myWorld.addTest(VISIBLE, new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 }) ;
		myWorld.addTest(VISIBLE, tab) ;
		myWorld.addTest(VISIBLE, tab2) ;
		myWorld.addTest(VISIBLE, new int[] { 17, -3, 1, 14, -13, 5, 1, 9, 18 }) ;
		myWorld.addTest(VISIBLE, new int[] { 21, 25, 1, 14, -13, 5, 1, 9, 18 }) ;
		myWorld.addTest(INVISIBLE, new int[] { 25, 21, 1, 14, -13, 5, 1, 9, 18 }) ;
		myWorld.addTest(INVISIBLE, new int[] { 24, 23, 1, 14, -13, 25, 1, 9, 18 }) ;
		myWorld.addTest(INVISIBLE, new int[] { 22, 24, 1, 14, -13, 25, 1, 9, 18 }) ;
		myWorld.addTest(INVISIBLE, new int[] { 22, 24, 1, 14, -13, 25, 1, 25, 18 }) ;
		myWorld.addTest(INVISIBLE, new int[] { 22, 24, 1, 14, -13, 24, 1, 25, 18 }) ;
		myWorld.addTest(INVISIBLE, new int[] { 24, 24, 1, 14, -13, 24, 1, 24, 18 }) ;
		myWorld.addTest(INVISIBLE, new int[] { 25, 25, 1, 14, -13, 24, 1, 24, 18 }) ;
		myWorld.addTest(INVISIBLE, new int[] { 22, 24, 1, 14, -13, 24, 1, 22, 18 }) ;
		myWorld.addTest(INVISIBLE, tab3) ;
		myWorld.addTest(INVISIBLE, tab4) ;

		templatePython("max2Value", new String[] {"Array[Int]"},
				"def max2Value(nums):\n",
				"  max=-10000000\n"+
				"  sec=-10000000\n"+
				"  for i in range(len(nums)):\n"+
				"    if nums[i] > max:\n"+
				"      sec = max\n"+
				"      max = nums[i]\n"+
				"    elif nums[i] > sec:\n"+
				"      sec = nums[i]\n"+
				"  return sec\n");
		templateScala("max2Value", new String[] {"Array[Int]"}, 
				"def max2Value(nums:Array[Int]): Int = {\n",
				"  var max=Integer.MIN_VALUE\n"+
				"  var sec=Integer.MIN_VALUE\n"+
				"  for (i <- 0 to nums.length-1)\n"+
				"    if (nums(i) > max) {\n"+
				"      sec = max\n"+
				"      max = nums(i)\n"+
				"    } else if (nums(i) > sec) {\n"+
				"      sec = nums(i)\n"+
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
		int max = Integer.MIN_VALUE;
		int sec = Integer.MIN_VALUE;
		for (int i=0; i<tab.length; i++) 
			if (tab[i] > max) {
				sec = max;
				max = tab[i];
			} else if (tab[i] > sec) {
				sec = tab[i];
			}
				
		return sec;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}




