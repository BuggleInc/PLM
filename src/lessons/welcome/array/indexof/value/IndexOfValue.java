package lessons.welcome.array.indexof.value;

import java.util.Random;

import jlm.core.model.Game;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class IndexOfValue extends BatExercise {

	Random r = new Random();
	
	private int getIndex(int[] tab) {
		return tab[r.nextInt(tab.length)];
	}
	
	public IndexOfValue(Lesson lesson) {
		super(lesson);

		int[] tab = new int[15];
		for (int i=0; i<tab.length; i++) 
			tab[i] = r.nextInt(35)-15;

		int[] tab2 = new int[20];
		for (int i=0; i<tab2.length; i++) 
			tab2[i] = r.nextInt(35)-15;

		int[] tab3 = new int[25];
		for (int i=0; i<tab3.length; i++) 
			tab3[i] = r.nextInt(35)-15;
		
		int[] tab4 = new int[25];
		for (int i=0; i<tab4.length; i++) 
			tab4[i] = r.nextInt(35)-15;

		BatWorld myWorld = new BatWorld("indexOfMaxValue");
		myWorld.addTest(VISIBLE, new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 }, 17) ;
		myWorld.addTest(VISIBLE, new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 }, 15) ;
		myWorld.addTest(VISIBLE, tab, r.nextInt(35)-15);
		myWorld.addTest(VISIBLE, tab, getIndex(tab));
		myWorld.addTest(VISIBLE, tab2, r.nextInt(35)-15);
		myWorld.addTest(VISIBLE, tab2, getIndex(tab2));
		myWorld.addTest(INVISIBLE, tab3, r.nextInt(35)-15);
		myWorld.addTest(INVISIBLE, tab3, getIndex(tab3));
		myWorld.addTest(INVISIBLE, tab4, r.nextInt(35)-15);
		myWorld.addTest(INVISIBLE, tab4, getIndex(tab4));

		langTemplate(Game.PYTHON, "indexOfValue", 
				"def indexOfMaxValue(nums,val):\n",
				"  for i in range(len(nums)):\n" +
				"    if nums[i]==val:\n"+
				"      return i\n" +
				"  return -1\n");
		
		setup(myWorld);
	}
	/* BEGIN SKEL */
	public void run(BatTest t) {
		t.setResult( indexOf( (int[])t.getParameter(0), (Integer)t.getParameter(1) ) );
	}
	/* END SKEL */

	/* BEGIN TEMPLATE */
	// computes the index of the first value equals to 'lookingFor' contained in tab variable
	public int indexOf(int[] tab, Integer lookingFor) {
		/* BEGIN SOLUTION */
		for (int i=0; i<tab.length; i++) 
			if (tab[i] == lookingFor) 
				return i;
		
		return -1;
		/* END SOLUTION */
	}

	/* END TEMPLATE */
}




