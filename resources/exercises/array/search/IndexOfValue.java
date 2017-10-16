package array.search;

import java.util.Random;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class IndexOfValue extends ExerciseTemplated {

	Random r = new Random();
	
	private int getIndex(int[] tab) {
		return tab[r.nextInt(tab.length)];
	}
	
	public IndexOfValue(FileUtils fileUtils) {
		super("IndexOfValue");

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

		BatWorld myWorld = new BatWorld(fileUtils, "indexOfValue");
		myWorld.addTest(BatTest.VISIBLE, new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 }, 17) ;
		myWorld.addTest(BatTest.VISIBLE, new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 }, 15) ;
		myWorld.addTest(BatTest.VISIBLE, tab, r.nextInt(35)-15);
		myWorld.addTest(BatTest.VISIBLE, tab, getIndex(tab));
		myWorld.addTest(BatTest.VISIBLE, tab2, r.nextInt(35)-15);
		myWorld.addTest(BatTest.VISIBLE, tab2, getIndex(tab2));
		myWorld.addTest(BatTest.INVISIBLE, tab3, r.nextInt(35)-15);
		myWorld.addTest(BatTest.INVISIBLE, tab3, getIndex(tab3));
		myWorld.addTest(BatTest.INVISIBLE, tab4, r.nextInt(35)-15);
		myWorld.addTest(BatTest.INVISIBLE, tab4, getIndex(tab4));
		
		setup(myWorld);
	}
}
