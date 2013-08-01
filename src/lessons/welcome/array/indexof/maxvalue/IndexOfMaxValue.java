package lessons.welcome.array.indexof.maxvalue;

import java.util.Random;

import jlm.core.model.Game;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class IndexOfMaxValue extends BatExercise {

	public IndexOfMaxValue(Lesson lesson) {
		super(lesson);
		Random r = new Random();
		
		int[] tab = new int[15];
		for (int i=0; i<tab.length; i++) 
			tab[i] = r.nextInt(35);

		int[] tab2 = new int[25];
		for (int i=0; i<tab2.length; i++) 
			tab2[i] = r.nextInt(35);

		BatWorld myWorld = new BatWorld("indexOfMaxValue");
		myWorld.addTest(VISIBLE, new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 }) ;
		myWorld.addTest(VISIBLE, tab) ;
		myWorld.addTest(INVISIBLE, tab2) ;

		langTemplate(Game.PYTHON, "indexOfMaxValue", 
				"def indexOfMaxValue(nums):\n",
				"  max=nums[0]\n" +
				"  maxIdx = 0\n" +
				"  for i in range(len(nums)):\n" +
				"    if nums[i]>max:\n"+
				"      max = nums[i]\n" +
				"      maxIdx = i\n"+
				"  return maxIdx\n");

		setup(myWorld);
	}
	
	/* BEGIN SKEL */
	public void run(BatTest t) {
		t.setResult( indexOfMaximum( (int[])t.getParameter(0)) );
	}
	/* END SKEL */

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




