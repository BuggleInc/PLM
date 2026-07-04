package lessons.welcome.array.search;

import java.util.Random;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class OccurrenceOfValue extends BatExercise {

	Random r = new Random();
	
	private int getIndex(int[] tab) {
		return tab[r.nextInt(tab.length)];
	}
	
	public OccurrenceOfValue(Lesson lesson) {
		super(lesson);
		
		int[] tab = new int[15];
		for (int i=0; i<tab.length; i++) 
			tab[i] = r.nextInt(tab.length/2);

		int[] tab2 = new int[20];
		for (int i=0; i<tab2.length; i++) 
			tab2[i] = r.nextInt(tab.length/2);

		int[] tab3 = new int[25];
		for (int i=0; i<tab3.length; i++) 
			tab3[i] = r.nextInt(tab.length/2);
		
		int[] tab4 = new int[25];
		for (int i=0; i<tab4.length; i++) 
			tab4[i] = r.nextInt(tab.length/2);

		BatWorld myWorld = new BatWorld("occurrences");
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

                setup(myWorld);
        }
}
