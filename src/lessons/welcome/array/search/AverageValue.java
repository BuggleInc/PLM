package lessons.welcome.array.search;

import java.util.Random;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class AverageValue extends BatExercise {

	public AverageValue(Lesson lesson) {
		super(lesson);
		
		int[] randomTab = new int[30];
		Random r = new Random();
		for (int i=0; i<randomTab.length; i++) 
			randomTab[i] = r.nextInt(35);
		
		BatWorld myWorld = new BatWorld("averageValue");
		myWorld.addTest(VISIBLE, new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 });
		myWorld.addTest(VISIBLE, randomTab);
		myWorld.addTest(VISIBLE, new int[] {1, 1, 2, 3, 1}) ;
		myWorld.addTest(VISIBLE, new int[] {1, 1, 2, 4, 1}) ;
		myWorld.addTest(VISIBLE, new int[] {1, 1, 2, 1, 2, 3}) ;
		myWorld.addTest(INVISIBLE, new int[] {1, 1, 2, 1, 2, 1}) ;
		myWorld.addTest(INVISIBLE, new int[] {1, 2, 3, 1, 2, 3}) ;
		myWorld.addTest(INVISIBLE, new int[] {1, 2, 3}) ;
		myWorld.addTest(INVISIBLE, new int[] {1, 1, 1}) ;
		myWorld.addTest(INVISIBLE, new int[] {1, 2}) ;
		myWorld.addTest(INVISIBLE, new int[] {42}) ;

                setup(myWorld);
        }
}
