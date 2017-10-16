package array.search;

import java.util.Random;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class MaxValue extends ExerciseTemplated {

	public MaxValue(FileUtils fileUtils) {
		super("MaxValue");
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

		BatWorld myWorld = new BatWorld(fileUtils, "maxValue");
		myWorld.addTest(BatTest.VISIBLE, new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 }) ;
		myWorld.addTest(BatTest.VISIBLE, tab) ;
		myWorld.addTest(BatTest.VISIBLE, tab2) ;
		myWorld.addTest(BatTest.INVISIBLE, tab3) ;
		myWorld.addTest(BatTest.INVISIBLE, tab4) ;

		setup(myWorld);
	}
}
