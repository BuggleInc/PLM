package recursion.cons.occurrences;

import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;

public class Occurrences extends ConsExercise {

	public Occurrences() {
		super("Occurrences", "Occurrences");
		
		BatWorld myWorld = new ConsWorld("occurrences");
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 2, 4}), 1);
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 2, 4}), 2);
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 3, 4}), 42);
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 1, 1}), 1);
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 1, 3, 2}), 2) ;
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{2, 4, 6, 8, 10}), -2) ;
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{}), 42) ;

		setup(myWorld);
	}
}
