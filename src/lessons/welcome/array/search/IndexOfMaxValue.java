package lessons.welcome.array.search;

import java.util.Random;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class IndexOfMaxValue extends BatExercise {

	public IndexOfMaxValue(Game game, Lesson lesson) {
		super(game, lesson);
		Random r = new Random(0);
		
		int[] tab = new int[15];
		for (int i=0; i<tab.length; i++) 
			tab[i] = r.nextInt(35);

		int[] tab2 = new int[25];
		for (int i=0; i<tab2.length; i++) 
			tab2[i] = r.nextInt(35);

		BatWorld myWorld = new BatWorld(game, "indexOfMaxValue");
		myWorld.addTest(VISIBLE, new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 }) ;
		myWorld.addTest(VISIBLE, tab) ;
		myWorld.addTest(INVISIBLE, tab2) ;
		myWorld.addTest(INVISIBLE, new int[] { -4, -3, -1, -17, -13, -5, -3, -1, -9, -18 }) ;

		templatePython("indexOfMaxValue", new String[]{"Array[Int]"},
				"def indexOfMaxValue(nums):\n",
				"  max=nums[0]\n" +
				"  maxIdx = 0\n" +
				"  for i in range(len(nums)):\n" +
				"    if nums[i]>max:\n"+
				"      max = nums[i]\n" +
				"      maxIdx = i\n"+
				"  return maxIdx\n");
		templateScala("indexOfMaxValue", new String[]{"Array[Int]"},
				"def indexOfMaxValue(nums:Array[Int]):Int = {\n",
				"  var max=nums(0)\n" +
				"  var maxIdx = 0\n" +
				"  for (i <- 0 to nums.length-1)\n" +
				"    if (nums(i)>max) {\n"+
				"      max = nums(i)\n" +
				"      maxIdx = i\n"+
				"    }\n"+
				"  return maxIdx\n"+
				"}");

		setup(myWorld);
	}
	
	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( indexOfMaximum( (int[])t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	// computes the index of the maximum of the values contained in tab variable
	public int indexOfMaximum(int[] tab) {
		/* BEGIN SOLUTION */
		int max = Integer.MIN_VALUE;
		int index = 0;
		for (int i=0; i<tab.length; i++) {
			if (tab[i] > max) { // we are looking for the first occurence
				max = tab[i];
				index = i;
			}
		}
		return index;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}




