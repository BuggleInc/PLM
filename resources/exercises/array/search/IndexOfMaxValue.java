package array.search;

import java.util.Random;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class IndexOfMaxValue extends ExerciseTemplated {

	public IndexOfMaxValue(Lesson lesson) {
		super("IndexOfMaxValue");
		Random r = new Random();
		
		int[] tab = new int[15];
		for (int i=0; i<tab.length; i++) 
			tab[i] = r.nextInt(35);

		int[] tab2 = new int[25];
		for (int i=0; i<tab2.length; i++) 
			tab2[i] = r.nextInt(35);

		BatWorld myWorld = new BatWorld("indexOfMaxValue");
		myWorld.addTest(BatTest.VISIBLE, new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 }) ;
		myWorld.addTest(BatTest.VISIBLE, tab) ;
		myWorld.addTest(BatTest.INVISIBLE, tab2) ;
		myWorld.addTest(BatTest.INVISIBLE, new int[] { -4, -3, -1, -17, -13, -5, -3, -1, -9, -18 }) ;

		setup(myWorld);
	}
}
